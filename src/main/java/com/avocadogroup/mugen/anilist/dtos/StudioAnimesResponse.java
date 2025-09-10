package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudioAnimesResponse {
    private StudioDto data; // The main data object containing studio details and media list
}
