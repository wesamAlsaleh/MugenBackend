package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.dtos.smallDtos.CoverImageDto;
import com.avocadogroup.mugen.anilist.dtos.smallDtos.NextAiringEpisodeDto;
import com.avocadogroup.mugen.anilist.dtos.smallDtos.TitleDto;
import lombok.Data;

import java.util.List;

@Data
public class TopAnimeDto {
    private int id;
    private TitleDto title;
    private CoverImageDto coverImage;
    private int averageScore;
    private int meanScore;
    private String status;
    private NextAiringEpisodeDto nextAiringEpisode;
}
