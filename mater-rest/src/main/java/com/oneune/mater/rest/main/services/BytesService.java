package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.store.dtos.BytesDto;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
import com.oneune.mater.rest.main.store.dtos.VideoDto;
import com.oneune.mater.rest.main.store.exceptions.BusinessLogicException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class BytesService {

    public PhotoDto handlePhotoFile(Update update, DefaultAbsSender bot) {
        List<PhotoSize> photos = update.getMessage().getPhoto();
        PhotoSize largestPhoto = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(() -> new BusinessLogicException("Not found a photo!"));
        try {
            File file = bot.execute(new GetFile(largestPhoto.getFileId()));
            java.io.File downloadedFile = bot.downloadFile(file);
            log.info("Фото успешно обработано!");
            return PhotoDto.builder()
                    .bytes(convertFileToBytes(downloadedFile))
                    .build();
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public VideoDto handleVideoFile(Update update, DefaultAbsSender bot) {

        Video video = update.getMessage().getVideo();

        try {
            File file = bot.execute(new GetFile(video.getFileId()));
            java.io.File downloadedFile = bot.downloadFile(file);
            log.info("Видео успешно обработано!");
            return VideoDto.builder()
                    .bytes(convertFileToBytes(downloadedFile))
                    .build();
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] convertFileToBytes(java.io.File file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = new java.io.FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer,0, length);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public boolean areMultimediaEquals(BytesDto leftOperand, BytesDto rightOperand) {
        return Arrays.equals(leftOperand.getBytes(), rightOperand.getBytes());
    }
}
