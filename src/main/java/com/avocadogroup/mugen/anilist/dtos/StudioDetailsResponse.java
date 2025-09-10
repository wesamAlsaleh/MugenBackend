package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudioDetailsResponse {
    private StudioDto data; // The main data object containing studio details and media list
}
