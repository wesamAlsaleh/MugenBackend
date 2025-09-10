package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.dtos.smallDtos.StudioMediaDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudioDto {
    private String name;
    @JsonProperty("isAnimationStudio") // This is required because the field name in the JSON response is "isAnimationStudio" and not "animationStudio"
    private boolean isAnimationStudio;
    private StudioMediaDto media;
}


