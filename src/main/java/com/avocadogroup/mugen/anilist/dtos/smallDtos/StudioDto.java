package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class StudioDto {
    private Integer id; // Unique identifier for the studio entry
    private boolean isMain; // Indicates if the studio is the main studio for the anime
    private StudioNodeDto node; // Studio details inside the node object
}
