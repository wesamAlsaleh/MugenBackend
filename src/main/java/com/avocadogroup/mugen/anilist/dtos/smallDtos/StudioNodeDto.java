package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import lombok.Data;

import java.util.List;

@Data
public class StudioNodeDto {
    private int id;
    private TitleDto title;
    private CoverImageDto coverImage;
    private StartDateDto startDate;
    private AnimeSeasons season;
    private Integer seasonYear;
    private int averageScore;
    private int meanScore;
    private String type;
    private String status;
    private int episodes;
    private List<String> genres; // List of genres
}
