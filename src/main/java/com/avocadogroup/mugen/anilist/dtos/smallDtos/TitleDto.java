package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TitleDto {
    private String english;
    @JsonProperty("native")
    private String nativeString;  // Java-safe name
    private String romaji;
    private String userPreferred;
}
