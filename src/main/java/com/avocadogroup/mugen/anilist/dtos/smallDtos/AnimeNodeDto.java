package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import com.avocadogroup.mugen.anilist.enums.MediaStatus;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.Data;

import java.util.List;

@Data
public class AnimeNodeDto {
    private int id;
    private TitleDto title;
    private CoverImageDto coverImage;
    private StartDateDto startDate;
    private AnimeSeasons season;
    private Integer seasonYear;
    private Integer averageScore;
    private Integer meanScore;
    private MediaTypes type;
    private MediaStatus status;
    private Integer episodes;
    private List<String> genres;
}
