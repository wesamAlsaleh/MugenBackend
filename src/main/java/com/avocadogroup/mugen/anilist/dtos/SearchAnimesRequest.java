package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SearchAnimesRequest {
    private int page = 1; // Default value of 1 if not provided
    private int perPage = 6; // Default value of 6 if not provided
    private MediaTypes type = MediaTypes.ANIME; // Default to ANIME if not provided
    private String searchQuery = ""; // Default to empty string if not provided
}
