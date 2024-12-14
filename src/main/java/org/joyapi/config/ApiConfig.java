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

        String cookies = "gdpr=1; user_id=USER_ID; pass_hash=PASSWORD_HASH; tag_blacklist=amputee%2520anus_focus%2520anus_hair%2520anus_juice%2520anus_only%2520anus_visible_through_clothes%2520cuntboy%2520disability%2520facesitting%2520fart%2520femboy%2520fisting%2520futanari%2520gay%2520killed%2520male%2Fmale%2520male_only%2520old%2520old_woman%2520photorealism%2520photorealistic%2520pig%2520pubes%2520pubic_hair%2520rimjob%2520rimming%2520scat%2520scp_foundation%2520skaven%2520spread_anus%2520spreading%2520tentacle%2520tentacles%2520turd%2520ugly%2520ugly_bastard%2520ugly_bitch%2520ugly_female%2520ugly_man%2520ugly_woman%2520yaoi%2520zoophilia; cf_clearance=7hflk15k.XS4Tj9vS1.p7gT9N_aSa7OPQUuzwQny6fs-1734208145-1.2.1.1-Eb3jJy9tZC3bMWGEdSY53qvYlxVk63Px71YYv_4cO2MlQ1wt6tAYp1dSzxQ5D3BvXTOrZrfR2FSuA7Hm0hk1C7SVqZMWmsnbIpLM15YOZKp28wwJmC4kI_OCrVg8R4t.J4Yo1cTU2RvAB3d7zSHObXHkuMjnWA8YdIRiEFEgkMpeq4sae41jNJ4NFkiqnS1ywr2rKLBLri9oORUQb4PaT_JUfrTBAHkBJoKj97vgo_APZYvokk8fQUyo4dqtXAxsASu0KDbBL94nv.DGrUJcfMJ_6MH8U1af18QSBUCBvtp_Sk8DQRtxKa9dMfvLsrVfWPVj5or5r0tmZVvqAHFw5ApTEwmDIhxGZyA_OXkilnEjjnsmp7LM8DN_Bq8amFn8WqIllXJh8_1ydJYZfAWdBCJHxeEIAlVytLQYq8wDQRU";
        apiClient.addDefaultHeader("Cookie", cookies);

        apiClient.setDebugging(true);

        return api;
    }
}
