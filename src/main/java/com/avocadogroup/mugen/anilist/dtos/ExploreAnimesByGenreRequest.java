package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExploreAnimesByGenreRequest {
    private Integer page;
    private Integer perPage;
    private AnimeSeasons season;
    private Integer seasonYear;
    private MediaTypes type;
    private List<String> genres;
}
