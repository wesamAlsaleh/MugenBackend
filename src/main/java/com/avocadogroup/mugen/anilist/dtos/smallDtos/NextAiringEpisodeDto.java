package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class NextAiringEpisodeDto {
    private Integer airingAt; // Timestamp until the episode airs
    private Integer episode; // Episode number to be used as (10 of the total 12 episodes)
    private Long timeUntilAiring; // Seconds until episode airs (Countdown for Anime Details page ONLY!)
}


//  @JsonIgnore // ← Won’t be read from JSON, only set manually