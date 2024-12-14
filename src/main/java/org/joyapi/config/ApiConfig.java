package org.joyapi.config;

import com.source.client.api.DefaultApi;
import com.source.client.invoker.ApiClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ApiConfig {

    @Bean
    public DefaultApi defaultApi(){
        DefaultApi api = new DefaultApi();
        ApiClient apiClient = api.getApiClient();
        apiClient.addDefaultHeader("User-Agent", "PostmanRuntime/7.43.0");
        apiClient.addDefaultHeader("Postman-Token", "2ea04b43-bdc5-4635-abd6-53ce33845f4c");
//        apiClient.addDefaultHeader("Accept-Language", "ru");
        apiClient.addDefaultHeader("Accept-Encoding", "gzip, deflate, br");
        apiClient.addDefaultHeader("Cache-Control", "no-cache");
        apiClient.addDefaultHeader("Host", "api.source.com");
        apiClient.addDefaultHeader("Connection", "keep-alive");
        apiClient.setDebugging(true);

        return api;
    }
}
