package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudioEdgeDto {
    private AnimeNodeDto node; // Studio node containing anime details
    private Boolean isMainStudio;
}
