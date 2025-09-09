package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class VoiceActorDto {
    private Long id; // The ID of the voice actor
    private CharacterNameDto name; // The name of the voice actor
    private CharacterImageDto image;
    private String siteUrl; // The URL of the voice actor on AniList
}
