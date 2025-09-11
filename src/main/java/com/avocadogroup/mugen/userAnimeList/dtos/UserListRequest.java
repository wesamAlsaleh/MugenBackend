package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.AnimeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListRequest {
    private int page; // Page number, default is 1
    private int perPage; // Number of items per page, default is 10
    private AnimeStatus status; // Anime status filter, default is WATCHING
}
