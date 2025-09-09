package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnimeListResponse {
    private List<AnimeDto> data; // List of objects of type AnimeDto
}
