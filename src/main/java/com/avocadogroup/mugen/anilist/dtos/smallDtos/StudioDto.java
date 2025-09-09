package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class StudioDto {
    private Integer id;
    private String name;
    private String siteUrl;
    private Boolean isAnimationStudio;
}
