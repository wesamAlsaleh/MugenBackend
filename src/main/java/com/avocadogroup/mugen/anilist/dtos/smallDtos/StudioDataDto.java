package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudioDataDto {
    private String name;
    @JsonProperty("isAnimationStudio") // This is required because the field name in the JSON response is "isAnimationStudio" and not "animationStudio"
    private boolean isAnimationStudio;
    private StudioMediaDto media;
}
