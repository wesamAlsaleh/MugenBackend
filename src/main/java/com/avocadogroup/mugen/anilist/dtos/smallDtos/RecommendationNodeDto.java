package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class RecommendationNodeDto {
    private Integer rating; // The rating given to the recommendation
    private MediaRecommendationDto mediaRecommendation; // The recommended media details
}
