package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.common.aop.annotations.LogExecutionDuration;
import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.contracts.Identifiable;
import com.oneune.mater.rest.main.events.CarUpdatedEvent;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.readers.FileReader;
import com.oneune.mater.rest.main.repositories.CarFileRepository;
import com.oneune.mater.rest.main.repositories.CarRepository;
import com.oneune.mater.rest.main.repositories.UserFavoriteCarLinkRepository;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.dtos.files.FileDto;
import com.oneune.mater.rest.main.store.entities.*;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.swing.*;
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
    UserService userService;
    UserFavoriteCarLinkRepository userFavoriteCarLinkRepository;
    ApplicationEventPublisher applicationEventPublisher;

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
        CarEntity carEntity = carReader.getEntityById(carId);
        modelMapper.map(carDto, carEntity);
        carRepository.saveAndFlush(carEntity);
        applicationEventPublisher.publishEvent(new CarUpdatedEvent(this, carId));
        return carReader.getById(carId);
    }

    @Transactional
    @Override
    public CarDto deleteById(Long carId) {
        CarEntity carEntity = carReader.getEntityById(carId);
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

        files = Objects.isNull(files) ? new ArrayList<>() : files;
        CarEntity carEntity = getEntityById(carId);
        List<CarFileEntity> existingCarFiles = carEntity.getFiles();
        carFileRepository.deleteAllById(Identifiable.extractIds(existingCarFiles, SortOrder.UNSORTED));
        existingCarFiles.clear();

        selectelS3Service.uploadFiles(files);
        Map<String, Pair<String, Long>> filesMeta = selectelS3Service.getFilesMeta(
                files.stream().map(MultipartFile::getOriginalFilename).toList()
        );

        List<CarFileEntity> carFileEntities = files.stream()
                .map(file -> CarFileEntity.builder()
                        .car(carEntity)
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .size(filesMeta.get(file.getOriginalFilename()).getSecond())
                        .url(filesMeta.get(file.getOriginalFilename()).getFirst())
                        .build())
                .collect(Collectors.toList());

        existingCarFiles.addAll(carFileEntities);
        carRepository.saveAndFlush(carEntity);
        carFileRepository.flush();

        return CompletableFuture.completedFuture(modelMapper.map(carFileEntities, FileReader.FILE_DTO_LIST));
    }

    public List<CarDto> getAll() {
        return carReader.getAll();
    }

    @Transactional
    public void postFavoriteCar(Long userId, Long carId) {
        UserEntity userEntity = userService.getEntityById(userId);
        CarEntity carEntity = getEntityById(carId);
        UserFavoriteCarLinkEntity userFavoriteCarLinkEntity = UserFavoriteCarLinkEntity.builder()
                .user(userEntity)
                .car(carEntity)
                .build();
        userFavoriteCarLinkRepository.save(userFavoriteCarLinkEntity);
    }

    public List<CarDto> getFavoritesByUserId(Long userId) {
        return carReader.getFavoritesByUserId(userId);
    }

    @Transactional
    public void deleteFavoriteCar(Long userId, Long carId) {
        UserFavoriteCarLinkEntity userFavoriteCarLinkEntity = carReader.getFavoriteLinkByIds(userId, carId);
        userFavoriteCarLinkRepository.delete(userFavoriteCarLinkEntity);
    }
}
