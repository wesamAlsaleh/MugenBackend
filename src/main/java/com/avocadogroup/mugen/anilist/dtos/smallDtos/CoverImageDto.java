package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoverImageDto {
    private String color;
    @JsonProperty("extraLarge")
    private String extraLargeCoverImageUrl;
    @JsonProperty("large")
    private String largeCoverImageUrl;
    @JsonProperty("medium")
    private String mediumCoverImageUrl;
}
