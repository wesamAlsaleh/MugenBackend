package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.UserAnimeProgress;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListsDto {
    @JsonInclude(JsonInclude.Include.NON_NULL) // Include in JSON only if not null
    private String progressStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL) // Include in JSON only if not null
    private boolean inFavorites;
}
