package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.dtos.smallDtos.*;
import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import com.avocadogroup.mugen.anilist.enums.MediaFormat;
import com.avocadogroup.mugen.anilist.enums.MediaStatus;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.Data;

import java.util.List;

@Data
public class AnimeDetailsDto {
    private Integer id;
    private Integer idMal;
    private TitleDto title;
    private MediaTypes type;
    private MediaFormat format;
    private MediaStatus status;
    private String description;
    private StartDateDto startDate;
    private EndDateDto endDate;
    private AnimeSeasons season;
    private Integer seasonYear;
    private Integer episodes;
    private Integer duration;
    private String countryOfOrigin;
    private String source;
    private String hashtag;
    private TrailerDto trailer;
    private CoverImageDto coverImage;
    private String bannerImage;
    private List<String> genres;
    private Integer averageScore;
    private Integer meanScore;
    private Integer popularity;
    private Integer trending;
    private StudiosDto studios;
    private charactersDto characters;
    private boolean isAdult;
    private NextAiringEpisodeDto nextAiringEpisode;
    private String siteUrl;
}
