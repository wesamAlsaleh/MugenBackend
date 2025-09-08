package com.avocadogroup.mugen.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AnilistConfig {
    @Value("${spring.anilist.api-url}")
    private String apiUrl;
}
