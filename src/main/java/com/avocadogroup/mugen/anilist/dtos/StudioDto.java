package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.dtos.smallDtos.StudioMediaDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore any unknown properties in the JSON response (without this line, it throws an error the JSON has extra fields)
public class StudioDto {
    private String name;
    private boolean isAnimationStudio;
    private StudioMediaDto media;
}
