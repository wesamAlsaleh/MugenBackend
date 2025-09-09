package com.avocadogroup.mugen.userAnimeList.dtos;

import com.avocadogroup.mugen.userAnimeList.AnimeStatus;
import lombok.Data;

@Data
public class UserListRequest {
    private int page = 1; // Page number, default is 1
    private int perPage = 10; // Number of items per page, default is 10
    private AnimeStatus status = AnimeStatus.WATCHING; // Anime status filter, default is WATCHING
}
