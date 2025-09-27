package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class CharactersDto {
    private List<CharacterDto> edges; // The characters have nodes which is a list of CharacterDto
}

