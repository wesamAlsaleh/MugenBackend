package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchAnimesRequest {
    private int page;
    private int perPage;
    private MediaTypes type;
    private String searchQuery;
}
