package com.avocadogroup.mugen.anilist.services;

import com.avocadogroup.mugen.anilist.dtos.AnimeDto;
import com.avocadogroup.mugen.anilist.dtos.ThisSeasonAnimesRequest;
import com.avocadogroup.mugen.anilist.dtos.ThisSeasonAnimesResponse;
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

    // Function to fetch this season's anime list using graphql
    public ThisSeasonAnimesResponse fetchThisSeasonAnime(ThisSeasonAnimesRequest paginationRequest)  {
        // Create a RestTemplate instance to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

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

        // Build request payload in a map {query: "...", variables: {...}}
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("query", query);
        requestPayload.put("variables", variables);

        // Set up HTTP headers for the request
        HttpHeaders headers = new HttpHeaders();

        // Set the content type to application/json
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with headers and payload as a JSON string {requestPayload: {...}, headers: {...}}
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

        // Try to make the POST request to the Anilist GraphQL API
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                anilistConfig.getApiUrl(), // Url
                org.springframework.http.HttpMethod.POST, // HTTP method
                request, // Request entity
                String.class // Response type
            );

            // Get the response body
            String responseBody = response.getBody();

            // Parse the response body using Jackson ObjectMapper to extract data
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the response body as a JsonNode tree structure
            JsonNode root = objectMapper.readTree(responseBody);

            // Jump to the "media" node to get the list of animes (Array of media objects)
            JsonNode mediaNode = root.path("data").path("Page").path("media");

            // Convert the "media" node to a list of AnimeDto objects (using TypeReference for generic type, now its empty which means List<AnimeDto>)
            List<AnimeDto> mediaList = objectMapper.convertValue(mediaNode, new TypeReference<>() {});

            // Return the list of animes
            return new ThisSeasonAnimesResponse(mediaList);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}


// TODO: infinite scrolling, by making the frontend call this endpoint with incrementing page numbers