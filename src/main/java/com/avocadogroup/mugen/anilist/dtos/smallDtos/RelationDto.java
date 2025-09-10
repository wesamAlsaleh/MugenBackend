package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

@Data
public class RelationDto {
    private String relationType;
    private RelationNodeDto node;
}
