package com.avocadogroup.mugen.favoriteAnimes.dtos;

import lombok.Data;

@Data
public class UserFavoriteListRequest {
    private int page = 1; // Default value of 1 if not provided
    private int perPage = 6; // Default value of 6 if not provided
}
