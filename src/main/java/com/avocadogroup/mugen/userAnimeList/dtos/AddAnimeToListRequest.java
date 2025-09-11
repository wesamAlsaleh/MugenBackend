package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.AnimeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddAnimeToListRequest {
    private Long animeId;
    private AnimeStatus status;
}
