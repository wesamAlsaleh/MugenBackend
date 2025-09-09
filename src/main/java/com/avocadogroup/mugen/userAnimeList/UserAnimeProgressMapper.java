package com.avocadogroup.mugen.userAnimeList;

import com.avocadogroup.mugen.userAnimeList.dtos.AddAnimeToListRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAnimeProgressMapper {
    // To create a new UserAnimeProgress entity from the request DTO
    UserAnimeProgress toEntity(AddAnimeToListRequest request);
}
