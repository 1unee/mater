package com.oneune.mater.rest.main.utils;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
@Log4j2
public class ImageUtils {

    public static final double DEFAULT_QUALITY = 0.9;
    public static final double DEFAULT_COMPRESS_MULTIPLIER = 0.7;

    /**
     * Сжимает изображение пропорционально переданному множителю.
     *
     * @param file       Исходный файл изображения.
     * @param multiplier Множитель для сжатия (например, 0.5 для уменьшения на 50%).
     * @return Сжатое изображение в виде массива байт.
     */
    public byte[] compressImage(MultipartFile file,
                                @Min(0) @Max(1) double quality,
                                @Min(0) @Max(1) double multiplier) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            Dimension dimensions = compressDimensions(originalImage, multiplier);
            Thumbnails.of(originalImage)
                    .size(dimensions.width, dimensions.height)
                    .outputQuality(quality)
                    .outputFormat(file.getContentType().split("/")[1])
                    .toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вычисляет новые размеры изображения с учетом множителя.
     *
     * @param image      Исходное изображение.
     * @param multiplier Множитель для изменения размера.
     * @return Новые размеры изображения.
     */
    private Dimension compressDimensions(BufferedImage image, double multiplier) {
        int compressedWidth = (int) (image.getWidth() * multiplier);
        int compressedHeight = (int) (image.getHeight() * multiplier);
        return new Dimension(compressedWidth, compressedHeight);
    }
}
