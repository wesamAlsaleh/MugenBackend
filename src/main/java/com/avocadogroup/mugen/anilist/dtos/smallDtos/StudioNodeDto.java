package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class StudioNodeDto {
    private Integer id; // Unique identifier for the studio
    private String name;
    private String siteUrl;
    private Boolean isAnimationStudio;
}
