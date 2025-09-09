package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class NodeDto {
    private Long id;
    private CharacterNameDto name;
    private String age;
    private String gender;
    private String description;
    private CharacterImageDto image;
    private String siteUrl;
}
