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

    public File downloadImage(String messageText){
        try {
            return downloadImageExternal(messageText);
        } catch (IOException e) {
            throw new ImageDownloadException("Error occurred during image download! URL: " + messageText);
        }
    }

    private File downloadImageExternal(String url) throws IOException {
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
