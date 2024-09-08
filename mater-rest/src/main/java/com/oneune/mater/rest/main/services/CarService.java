package com.oneune.mater.rest.main.services;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.repositories.CarRepository;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
import com.oneune.mater.rest.main.store.dtos.VideoPartDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.PhotoEntity;
import com.oneune.mater.rest.main.store.entities.SellerEntity;
import com.oneune.mater.rest.main.store.entities.VideoEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

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

    @Transactional
    public void putPhotos(Long carId, List<PhotoDto> photos) {
        CarEntity carEntity = carReader.getEntityById(carId);
        carEntity.getPhotos().clear();
        List<PhotoEntity> photoEntities = modelMapper.map(photos, TypeToken.getParameterized(List.class, PhotoEntity.class).getType());
        photoEntities.forEach(photoEntity -> photoEntity.setCar(carEntity));
        carEntity.getPhotos().addAll(photoEntities);
        carRepository.saveAndFlush(carEntity);
    }

    @Transactional
    public void deleteVideos(Long carId) {
        CarEntity carEntity = carReader.getEntityById(carId);
        carEntity.getVideos().clear();
        carRepository.saveAndFlush(carEntity);
    }

    @SneakyThrows
    @Transactional
    public void putVideos(Long carId, VideoPartDto videoPart) {

        Path tempDirectoryPath = Path.of("temp", videoPart.getName());

        if (!Files.exists(tempDirectoryPath)) { Files.createDirectories(tempDirectoryPath); }

        Path tempFile = tempDirectoryPath.resolve("%s.part".formatted(videoPart.getIndex())); // Создаем временный файл для хранения чанков

        // Сохраняем чанк в файл
        Files.writeString(tempFile, videoPart.getBase64());

        // Проверяем, все ли части присутствуют
        List<Path> partVideoPaths = Files.list(tempDirectoryPath)
                .filter(Files::isRegularFile)
                .toList();
        boolean isAllPartShared = partVideoPaths.size() == (long) videoPart.getAmount();

        // Проверяем, все ли части присутствуют
        if (isAllPartShared) {

            StringBuilder stringBuilder = new StringBuilder();

            for (Path partFile : partVideoPaths) {
                String base64Part = Files.readString(partFile);
                stringBuilder.append(base64Part);
            }

            CarEntity carEntity = carReader.getEntityById(carId);
            VideoEntity videoEntity = VideoEntity.builder()
                    .car(carEntity)
                    .name(videoPart.getName())
                    .type(videoPart.getType())
                    .base64(stringBuilder.toString())
                    .build();
            carEntity.getVideos().add(videoEntity);
            videoEntity.setCar(carEntity);
            carRepository.saveAndFlush(carEntity);

            Files.walk(tempDirectoryPath)
                    .sorted(Comparator.reverseOrder()) // Сначала удаляем файлы, потом папки
                    .forEach(filePath -> {
                        try {
                            Files.delete(filePath);
                        } catch (IOException e) {
                            log.error(e);
                        }
                    });

        } else {
            log.info(
                    "{}/{} part for video with name = {}",
                    partVideoPaths.size(),
                    videoPart.getAmount(),
                    videoPart.getName()
            );
        }
    }
}
