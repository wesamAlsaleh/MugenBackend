package com.avocadogroup.mugen.anilist.services;

import com.avocadogroup.mugen.anilist.dtos.*;
import com.avocadogroup.mugen.anilist.enums.AnimeSortBy;
import com.avocadogroup.mugen.configs.AnilistConfig;
import com.avocadogroup.mugen.global.exceptions.InternalServerErrorException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Note: Map is more similar to an object in JSON

@Service
@AllArgsConstructor
public class AnilistService {
    private final AnilistConfig anilistConfig;
    private final SeasonService seasonService;
    private final GraphQlService graphQlService;

    // Function to fetch this season's anime list using graphql
    public ThisSeasonAnimesResponse fetchThisSeasonAnimes(ThisSeasonAnimesRequest paginationRequest)  {
        // Prepare the GraphQL query
        String query = """
            query Query($page: Int, $perPage: Int, $season: MediaSeason, $seasonYear: Int) {
                       Page(page: $page, perPage: $perPage) {
                         media(season: $season, seasonYear: $seasonYear) {
                           id
                           title {
                             english
                             native
                             romaji
                             userPreferred
                           }
                           coverImage {
                             color
                             extraLarge
                             large
                             medium
                           }
                           averageScore
                           meanScore
                           type
                           status
                           episodes
                           genres
                           nextAiringEpisode {
                             airingAt
                             episode
                           }
                         }
                       }
                     }
        """;

        // Prepare the variables for the query in a map {page: 1, perPage: 10, season: "SPRING", seasonYear: 2024}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", paginationRequest.getPage());
        variables.put("perPage", paginationRequest.getPerPage());
        variables.put("season", seasonService.getCurrentSeason());
        variables.put("seasonYear", seasonService.getCurrentYear());

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        List<AnimeDto> mediaList = (List<AnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables); // Cast to List<AnimeDto> to avoid type mismatch

        // Return the list of animes wrapped in a ThisSeasonAnimesResponse object
        return new ThisSeasonAnimesResponse(mediaList);
    }

    // Function to fetch top this season's anime list based on average score
    public ThisSeasonTopAnimesResponse fetchThisSeasonTopAnimes() {
        // Prepare the GraphQL query
        String query = """
            query Query($perPage: Int, $season: MediaSeason, $seasonYear: Int, $sort: [MediaSort]) {
                Page(perPage: $perPage) {
                    media(season: $season, seasonYear: $seasonYear, sort: $sort) {
                        id
                        title {
                            english
                            native
                            romaji
                            userPreferred
                        }
                        averageScore
                        meanScore
                        status
                        nextAiringEpisode {
                            airingAt
                            episode
                        }
                    }
                }
            }
        """;

        // Prepare the variables for the query in a map {page: 1, perPage: 10, season: "SPRING", seasonYear: 2024}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", 15); // Hardcoded to get more results for top animes
        variables.put("season", seasonService.getCurrentSeason());
        variables.put("seasonYear", seasonService.getCurrentYear());
        variables.put("sort", AnimeSortBy.SCORE_DESC);

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        List<TopAnimeDto> mediaList = (List<TopAnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables); // Cast to List<TopAnimeDto> to avoid type mismatch

        // Return the list of animes
        return new ThisSeasonTopAnimesResponse(mediaList);
    }
}


// TODO: infinite scrolling, by making the frontend call this endpoint with incrementing page numbers