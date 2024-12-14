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
        apiClient.addDefaultHeader("Accept-Encoding", "identity");
        apiClient.addDefaultHeader("Cache-Control", "no-cache");
        apiClient.addDefaultHeader("Connection", "keep-alive");
        apiClient.setDebugging(true);

        return api;
    }
}
