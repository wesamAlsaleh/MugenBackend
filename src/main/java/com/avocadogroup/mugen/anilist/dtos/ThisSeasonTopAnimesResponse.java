package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ThisSeasonTopAnimesResponse {
    private List<TopAnimeDto> mediaList; // Array of TopAnimeDto objects
}
