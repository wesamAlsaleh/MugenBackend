package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class RelationsDto {
    private List<RelationDto> edges;
}
