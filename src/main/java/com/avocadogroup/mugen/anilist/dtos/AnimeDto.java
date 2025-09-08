package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.dtos.smallDtos.CoverImageDto;
import com.avocadogroup.mugen.anilist.dtos.smallDtos.TitleDto;
import com.avocadogroup.mugen.anilist.dtos.smallDtos.NextAiringEpisodeDto;
import lombok.Data;

import java.util.List;

@Data
public class AnimeDto {
    private int id;
    private TitleDto title;
    private CoverImageDto coverImage;
    private int averageScore;
    private int meanScore;
    private String type;
    private String status;
    private int episodes;
    private List<String> genres; // List of genres
    private NextAiringEpisodeDto nextAiringEpisode;
}


// Note: (no need for a separate DTO to the list of genres because it's raw array of strings, not an object)
// Right now, genres is a List like this: "genres": ["Action", "Comedy", "Drama"]
// Custom DTO for genres is not needed because it expects an object like this: "genres": { "genre1": "Action", "genre2": "Comedy" }
