package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import com.avocadogroup.mugen.anilist.enums.MediaFormat;
import com.avocadogroup.mugen.anilist.enums.MediaStatus;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import lombok.Data;

@Data
public class RelationNodeDto {
    private Integer id; // Relation Anime Id
    private TitleDto title;
    private CoverImageDto coverImage;
    private MediaTypes type;
    private MediaFormat format;
    private MediaStatus status;
    private Integer episodes;
    private String siteUrl;
}
