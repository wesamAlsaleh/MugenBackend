package com.avocadogroup.mugen.anilist.dtos;

import lombok.Data;



@Data
public class ThisSeasonAnimesRequest {
    private int page = 1; // Default to page 1
    private int perPage = 2; // Default to 15 items per page
}