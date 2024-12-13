package org.joyapi.service;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

@Service
public class ImageDownloader {

    public File downloadImage(String url) throws IOException {
        // Создаем HTTP клиент
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Создаем GET-запрос
            HttpGet request = new HttpGet(url);

            // Выполнение запроса
            HttpResponse response = client.execute(request);

            // Проверяем успешность запроса (статус 200)
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                File tempFile = File.createTempFile("downloaded", ".jpg");

                // Записываем данные в файл
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
