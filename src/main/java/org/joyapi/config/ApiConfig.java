package org.joyapi.config;

import com.source.client.api.DefaultApi;
import com.source.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Value("${api-client.request-debug-mode}")
    private boolean requestDebugMode;

    @Value("${api-client.user-agent}")
    private String userAgent;

    @Value("${api-client.accept-encoding}")
    private String acceptEncoding;

    @Value("${api-client.cache-control}")
    private String cacheControl;

    @Value("${api-client.connection}")
    private String connection;

    @Value("${api-client.coockies}")
    private String cookies;

    @Bean
    public DefaultApi defaultApi(){
        DefaultApi api = new DefaultApi();
        ApiClient apiClient = api.getApiClient();
        apiClient.addDefaultHeader("User-Agent", userAgent);
        apiClient.addDefaultHeader("Accept-Encoding", acceptEncoding);
        apiClient.addDefaultHeader("Cache-Control", cacheControl);
        apiClient.addDefaultHeader("Connection", connection);
//        apiClient.addDefaultHeader("Cookie", cookies);

        apiClient.setDebugging(requestDebugMode);

        return api;
    }
}
