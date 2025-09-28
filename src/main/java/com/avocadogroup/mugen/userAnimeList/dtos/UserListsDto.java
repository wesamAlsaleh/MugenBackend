package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.UserAnimeProgress;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListsDto {
//    @JsonInclude(JsonInclude.Include.NON_NULL) // Include in JSON only if not null
    private String progressStatus;

//    @JsonInclude(JsonInclude.Include.NON_NULL) // Include in JSON only if not null
    private Boolean inFavorites;
}

// Boolean == can be null, true or false, boolean == can be only true or false
// Integer == can be null, 0, 1, 2, ..., int == can be only 0, 1, 2, ...