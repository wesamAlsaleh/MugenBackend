package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnimeDetailsResponse {
    private List<AnimeDetailsDto> data;
}

// List<AnimeDetailsDto> instead of single AnimeDetailsDto because the GraphQL query is wrapped in a Page, so the response is a list of results, but in practice it will usually contain only one item.