package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.common.aop.annotations.LogExecutionDuration;
import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.readers.FileReader;
import com.oneune.mater.rest.main.repositories.CarFileRepository;
import com.oneune.mater.rest.main.repositories.CarRepository;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.dtos.FileDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.CarFileEntity;
import com.oneune.mater.rest.main.store.entities.SellerEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.utils.ImageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class CarService implements Command, CRUDable<CarDto, CarEntity> {

    ModelMapper modelMapper;
    CarRepository carRepository;
    CarReader carReader;
    ButtonBuilderService buttonBuilderService;
    SellerService sellerService;
    SelectelS3Service selectelS3Service;
    CarFileRepository carFileRepository;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        SendMessage sendCarsMenuKeyboardButtons = buttonBuilderService.buildCarsKeyboardButtons(update);
        TelegramBotUtils.uncheckedExecute(bot, sendCarsMenuKeyboardButtons);
    }

    @Transactional
    @Override
    public CarDto post(CarDto carDto) {
        CarEntity carEntity = new CarEntity();
        modelMapper.map(carDto, carEntity);
        carRepository.saveAndFlush(carEntity);
        return carReader.getById(carEntity.getId());
    }

    @Transactional
    public CarDto postByParams(Long sellerId, CarDto carDto) {
        SellerEntity sellerEntity = sellerService.getEntityById(sellerId);
        CarEntity carEntity = new CarEntity();
        modelMapper.map(carDto, carEntity);
        carEntity.setSeller(sellerEntity);
        carRepository.saveAndFlush(carEntity);
        return carReader.getById(carEntity.getId());
    }

    @Transactional
    @Override
    public CarDto put(Long carId, CarDto carDto) {
        // fix error on put
        CarEntity carEntity = carReader.getExcludeFiles(carId);
        modelMapper.map(carDto, carEntity);
        carRepository.saveAndFlush(carEntity);
        return carReader.getById(carId);
    }

    @Transactional
    @Override
    public CarDto deleteById(Long carId) {
        CarEntity carEntity = carReader.getExcludeFiles(carId);
        carRepository.delete(carEntity);
        carRepository.flush();
        return modelMapper.map(carEntity, CarDto.class);
    }

    public CarEntity getEntityById(Long carId) {
        return carReader.getEntityById(carId);
    }

    @Override
    public CarDto getById(Long carId) {
        return carReader.getById(carId);
    }

    @Override
    public PageResponse<CarDto> search(PageQuery pageQuery) {
        return carReader.search(pageQuery);
    }

    @Async
    @Transactional
    @LogExecutionDuration(logStartMessage = true)
    public CompletableFuture<List<FileDto>> putFiles(Long carId, List<MultipartFile> files) {

        // todo: сжимать фотки пропорционально
        files = Objects.isNull(files) ? new ArrayList<>() : files;
        CarEntity carEntity = getEntityById(carId);
        carEntity.getFiles().clear();

        selectelS3Service.uploadFiles(files,true);
        Map<String, String> fileUrls = selectelS3Service.getFileUrls(
                files.stream().map(MultipartFile::getOriginalFilename).toList()
        ); // Получаем URL файлов

        List<CarFileEntity> carFileEntities = files.stream()
                .map(file -> CarFileEntity.builder()
                        .car(carEntity)
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .size((long) ImageUtils.compressImage(
                                file, ImageUtils.DEFAULT_QUALITY, ImageUtils.DEFAULT_COMPRESS_MULTIPLIER
                        ).length)
                        .url(fileUrls.get(file.getOriginalFilename()))
                        .build())
                .collect(Collectors.toList());

        carEntity.getFiles().addAll(carFileEntities);
        carRepository.saveAndFlush(carEntity);
        carFileRepository.flush();

        return CompletableFuture.completedFuture(modelMapper.map(carFileEntities, FileReader.FILE_DTO_LIST));
    }
}
