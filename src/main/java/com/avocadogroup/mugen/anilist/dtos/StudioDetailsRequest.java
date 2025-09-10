package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudioDetailsRequest {
    private Integer page;
    private Integer perPage;
    private Integer studioId;
}
