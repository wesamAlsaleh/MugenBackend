package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class ThisSeasonAnimesRequest {
    private int page; // Default to page 1
    private int perPage; // Default to 15 items per page
}