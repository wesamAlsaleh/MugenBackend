package com.avocadogroup.mugen.anilist.services;

import com.avocadogroup.mugen.configs.AnilistConfig;
import com.avocadogroup.mugen.global.exceptions.BadRequestException;
import com.avocadogroup.mugen.global.exceptions.InternalServerErrorException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GraphQlService {
    private final AnilistConfig anilistConfig;

    /**
     * Method to builds a payload map for a GraphQL request.
     *
     * @param query     the GraphQL query string
     * @param variables the variables to be included in the request
     * @return a map containing the query and variables
     */
    private Map<String, Object> buildGraphQLPayload(String query, Map<String, Object> variables) {
        // Build request payload in a map {key: value}
        Map<String, Object> payload = new HashMap<>();

        // Fill the payload map with query and variables keys {"query": "...", "variables": {...}}
        payload.put("query", query);
        payload.put("variables", variables);

        // Return the constructed payload map example: {"query": "...", "variables": {...}}
        return payload;
    }

    /**
     * Sends a POST request to the AniList GraphQL API with the provided query and variables.
     *
     * @param query     the GraphQL query string to execute
     * @param variables a map of variables to include in the GraphQL request
     * @return a list of objects representing the "media" node from the response
     * @throws InternalServerErrorException if the request fails or the response cannot be parsed
     */
    public List<?> postGraphQLRequestToAnilist(String query, Map<String, Object> variables) {
        // Create a RestTemplate instance to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Set up HTTP headers for the request
        HttpHeaders requestHeaders = new HttpHeaders();

        // Set the content type to application/json
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body using the buildGraphQLPayload method
        Map<String, Object> requestBody = buildGraphQLPayload(query, variables);

        // Create the HTTP entity with headers and payload and return it as {"query": "...", "variables": {...}}
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, requestHeaders);

        // Try to make the POST request to the AniList GraphQL endpoint with the provided query and variables
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    anilistConfig.getApiUrl(), // Url
                    HttpMethod.POST, // HTTP method
                    request, // Request entity
                    String.class // Response type
            );

            // Get the response body to extract data using Jackson ObjectMapper
            String responseBody = response.getBody();

            // Parse the response body using Jackson ObjectMapper to extract data
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the response body as a JsonNode tree structure
            JsonNode root = objectMapper.readTree(responseBody);

            // Jump to the "media" node to get the list of animes (Array of media objects)
            JsonNode mediaNode = root.path("data").path("Page").path("media");

            // Convert the "media" node to a list of objects and return it (using TypeReference for generic type, now its empty which means List<Object>)
            return objectMapper.convertValue(mediaNode, new TypeReference<>() {});
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage()); // TODO: Log the error message and display to the client "Error fetching data from Anilist"
        }
    }
}
