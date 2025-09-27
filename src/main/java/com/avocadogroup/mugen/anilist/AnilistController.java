package com.avocadogroup.mugen.anilist;

import com.avocadogroup.mugen.anilist.dtos.*;
import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import com.avocadogroup.mugen.authentication.services.JwtService;
import com.avocadogroup.mugen.userAnimeList.UserAnimeProgressService;
import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// ModelAttribute is used to bind request parameters to a model object
// If the request is /this-season-animes?page=1&perPage=10, the modelAttribute will bind page=1 and perPage=10 to the ThisSeasonAnimesRequest object
// And if no parameters are provided, it will use the default values defined in the ThisSeasonAnimesRequest class.
// Example: /this-season-animes?page=4&perPage=5 will bind page=4 and perPage=5 to the ThisSeasonAnimesRequest object

// Use @ModelAttribute for binding query params into a DTO.
// Or use plain @RequestParam on method parameters if you don’t want a DTO.

@RestController
@RequestMapping("/anime")
@AllArgsConstructor
@Tag(name = "Anilist", description = "API endpoints for fetching anime data from Anilist GraphQL API")
public class AnilistController {
    private final AnilistService anilistService;
    private final JwtService jwtService;
    private final UserAnimeProgressService userAnimeProgressService;

    // API endpoint to get the season's anime list
    @GetMapping("/this-season-animes")
    public ResponseEntity<?> getThisSeasonAnimes(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "perPage", defaultValue = "15") int perPage
    ) {
        try {
        // Try to fetch this season animes from Anilist service (with pagination parameters)
        var mediaList = anilistService.fetchThisSeasonAnimes(new ThisSeasonAnimesRequest(page, perPage));

        // Return the fetched animes in the response body with HTTP status 200 OK
        return ResponseEntity.ok().body(mediaList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the current season animes based on the average score
    @GetMapping("/top-this-season-animes")
    public ResponseEntity<?> getTopThisSeasonAnimes() {
        try {
            // Try to fetch this season top animes from Anilist service based on average score
            var mediaList = anilistService.fetchThisSeasonTopAnimes();

            // Return the fetched animes in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(mediaList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // API endpoint for searching animes by title with pagination as query parameters!
    @GetMapping("/search-animes")
    public ResponseEntity<?> searchAnimes(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "type", defaultValue = "ANIME") MediaTypes type, // Default to ANIME, can be MANGA as well
            @RequestParam(name = "searchQuery", defaultValue = "") String searchQuery
    ) {
        try {
            // Try to fetch searched animes from Anilist service (based on title) with pagination parameters
            var mediaList = anilistService.searchAnimes(new SearchAnimesRequest(page, perPage, type, searchQuery));

            // Return the fetched animes in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(mediaList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to explore animes with filters like genres, season, seasonYear, type with pagination as query parameters
    @GetMapping("/explore-animes")
    public ResponseEntity<?> exploreAnimes(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "season" , required = false) AnimeSeasons season,
            @RequestParam(name = "seasonYear" , required = false) Integer seasonYear, // de
            @RequestParam(name = "type", defaultValue = "ANIME") MediaTypes type,
            @RequestParam(name = "genres", required = false) List<String> genres  // Spring will auto splits comma-separated values "&genres=ACTION,ROMANCE"
    ) {
        try {
            // Try to fetch the animes from Anilist service based on genres with pagination parameters
            var mediaList = anilistService.exploreAnimes(new ExploreAnimesByGenreRequest(page, perPage, season, seasonYear, type, genres));

            // Return the fetched animes in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(mediaList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the genres list based on user preferred language
    @GetMapping("/genres")
    public ResponseEntity<?> getGenres(
            @RequestParam(name = "lang", defaultValue = "EN") UserPreferredLanguage lang) {
        try {
            // Try to fetch the genres list from Anilist service
            var genres = anilistService.fetchGenres(lang);

            // Return the fetched genres in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(genres);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the anime details by its ID
    @GetMapping
    public ResponseEntity<?> getAnimeDetailsById(
            @RequestParam(name = "id") Integer animeId,
            @RequestParam(name = "token", required = false) String token // Optional token for authenticated requests
    ) {
        try {
            // Try to fetch the anime details from Anilist service based on anime ID
            var anime = anilistService.fetchAnimeDetailsById(animeId, token);

            // Return the fetched anime details in the response body with HTTP status 200 OK
            return ResponseEntity.ok(anime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the animes from for specific studio by its ID
    @GetMapping("/studio")
    public ResponseEntity<?> getStudioDetails(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "id") Integer studioId
    ) {
        try {
            // Try to fetch the animes from Anilist service based on studio ID with pagination parameters
            var studioDetails = anilistService.fetchStudioDetails(new StudioDetailsRequest(page, perPage, studioId));

            // Return the fetched animes in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(studioDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

