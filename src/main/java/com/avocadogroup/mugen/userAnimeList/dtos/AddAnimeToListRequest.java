package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.AnimeStatus;
import lombok.Data;

@Data
public class AddAnimeToListRequest {
    private Long animeId;
    private AnimeStatus status;
}
