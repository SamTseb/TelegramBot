package org.joyapi.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.joyapi.exception.ImageDownloadException;
import org.joyapi.exception.TelegramSendMediaException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

@Slf4j
@Service
public class MediaDownloadService {
    private static final long MAX_IMAGE_SIZE = 10L * 1024 * 1024;
    private static final long MAX_DIMENSIONS_LENGTH = 10000;
    private static final int MAX_DIMENSIONS_RATIO = 20;

    public File downloadMedia(String url){
        try {
            File media = downloadMediaExternal(url);
            if (!getMediaType(url).equals("mp4")) {
                if (!isResolutionAcceptable(media)) {
                    media = rescaleImage(media);
                }
                if (!isSizeAcceptable(media)) {
                    media = compressImage(media);
                }
            }
            return media;
        } catch (IOException e) {
            throw new ImageDownloadException(String.format("""
                                                Error occurred during image download!
                                                URL: %s
                                                Exception: %s""", url, e.getMessage()));
        }
    }

    private File downloadMediaExternal(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                File tempFile = File.createTempFile("downloaded", "." + getMediaType(url));

                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                return tempFile;
            } else {
                throw new IOException(String.format("""
                                                        Error occurred during image download!
                                                        URL: %s""", response.getStatusLine()));
            }
        }
    }

    private boolean isSizeAcceptable(File image) {
        return image.length() <= MAX_IMAGE_SIZE;
    }

    private boolean isResolutionAcceptable(File image) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(image);
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        if (imageHeight / imageWidth > MAX_DIMENSIONS_RATIO || imageWidth / imageHeight > MAX_DIMENSIONS_RATIO) {
            throw new TelegramSendMediaException(String.format("""
                                                    Image resolution is not acceptable!
                                                    Image: %s
                                                    Width: %d
                                                    Height: %d""", image.getName(), imageWidth, imageHeight),
                                                    null, null);
        }

        return (imageWidth + imageHeight) <= MAX_DIMENSIONS_LENGTH;
    }

    private File rescaleImage(File originalFile) {
        try {
            File rescaledFile = File.createTempFile("rescaled_", originalFile.getName());
            BufferedImage image = ImageIO.read(originalFile);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            float scalingFactor = 0.9f;
            float quality = 1;
            int currentWidth;
            int currentHeight;

            while (true) {
                currentWidth = (int) (imageWidth * scalingFactor);
                currentHeight = (int) (imageHeight * scalingFactor);
                Thumbnails.of(originalFile)
                        .size(currentWidth, currentHeight)
                        .outputQuality(quality)
                        .toFile(rescaledFile);

                if (isResolutionAcceptable(rescaledFile)) {
                    if (!originalFile.delete()){
                        log.warn("Unable to delete original file: {}", originalFile.getName());
                    }
                    return rescaledFile;
                }
                scalingFactor -= 0.1f;
            }
        } catch (IOException e) {
            throw new ImageDownloadException(String.format("""
                                                Error occurred during image resizing!
                                                Original file: %s
                                                Exception: %s""", originalFile.getName(), e.getMessage()));
        }
    }

    private File compressImage(File originalFile) {
        try {
            File resizedFile = File.createTempFile("resized_", originalFile.getName());
            BufferedImage image = ImageIO.read(originalFile);
            int currentWidth = image.getWidth();
            int currentHeight = image.getHeight();
            float quality = 0.9f;

            while (true) {
                Thumbnails.of(originalFile)
                        .size(currentWidth, currentHeight)
                        .outputQuality(quality)
                        .toFile(resizedFile);

                if (isSizeAcceptable(resizedFile)) {
                    if (!originalFile.delete()){
                        log.warn("Unable to delete original file: {}", originalFile.getName());
                    }
                    return resizedFile;
                }

                quality -= 0.1f;

                if (quality < 0.1f) {
                    quality = 0.9f;
                    currentWidth *= 0.9;
                    currentHeight *= 0.9;
                }

                if (currentWidth < 100 || currentHeight < 100) {
                    throw new IOException("Unable to reduce image size to below 10 MB");
                }
            }
        } catch (IOException e) {
            throw new ImageDownloadException(String.format("""
                                                Error occurred during image resizing!
                                                Original file: %s
                                                Exception: %s""", originalFile.getName(), e.getMessage()));
        }
    }

    public String getMediaType(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }
}
