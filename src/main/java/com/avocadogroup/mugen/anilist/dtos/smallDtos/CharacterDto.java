package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class CharacterDto {
    private String role; // Main, Supporting, Background
    private NodeDto node; // The character details inside a node object
    private List<VoiceActorDto> voiceActors; // The voice actors for the character (its a list because a character can have multiple voice actors in different languages)
}
