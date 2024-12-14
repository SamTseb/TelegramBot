package org.joyapi.service;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.joyapi.exception.ImageDownloadException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

@Service
public class ImageDownloadService {

    public File downloadImage(String url){
        try {
            return downloadImageExternal(url);
        } catch (IOException e) {
            throw new ImageDownloadException("Error occurred during image download! URL: " + url);
        }
    }

    private File downloadImageExternal(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                File tempFile = File.createTempFile("downloaded", ".jpg");

                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                return tempFile;
            } else {
                throw new IOException("Ошибка при скачивании изображения, статус: " + response.getStatusLine());
            }
        }
    }
}
