package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class StudioMediaDto {
    private List<StudioNodeDto> nodes; // List of studio nodes
}


// Note that, each node contains basic anime information like id, title, coverImage, etc.


