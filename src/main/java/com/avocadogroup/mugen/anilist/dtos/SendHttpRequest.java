package com.avocadogroup.mugen.anilist.dtos;

import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Data
public class SendHttpRequest {
    private HttpEntity<?> requestEntity; // e.g., new HttpEntity<>(body, headers)
}
