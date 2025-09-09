package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class charactersDto {
    private List<CharacterDto> nodes; // The characters have nodes which is a list of CharacterDto
}

