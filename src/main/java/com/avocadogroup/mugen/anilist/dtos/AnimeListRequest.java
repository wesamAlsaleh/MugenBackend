package com.avocadogroup.mugen.anilist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnimeListRequest {
    private int page;
    private int perPage;
    private List<Long> animeIds; // Array of anime IDs to fetch details for
}
