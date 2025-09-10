package com.avocadogroup.mugen.anilist;

import com.avocadogroup.mugen.anilist.dtos.*;
import com.avocadogroup.mugen.anilist.enums.AnimeSeasons;
import com.avocadogroup.mugen.anilist.enums.MediaStatus;
import com.avocadogroup.mugen.anilist.enums.MediaTypes;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ModelAttribute is used to bind request parameters to a model object
// If the request is /this-season-animes?page=1&perPage=10, the modelAttribute will bind page=1 and perPage=10 to the ThisSeasonAnimesRequest object
// And if no parameters are provided, it will use the default values defined in the ThisSeasonAnimesRequest class.
// Example: /this-season-animes?page=4&perPage=5 will bind page=4 and perPage=5 to the ThisSeasonAnimesRequest object

// Use @ModelAttribute for binding query params into a DTO.
// Or use plain @RequestParam on method parameters if you don’t want a DTO.

@RestController
@RequestMapping("/anime")
@AllArgsConstructor
public class AnilistController {
    private final AnilistService anilistService;

    // API endpoint to get the season's anime list
    @GetMapping("/this-season-animes")
    public ResponseEntity<?> getThisSeasonAnimes(@ModelAttribute ThisSeasonAnimesRequest request) {
        try {
        // Try to fetch this season animes from Anilist service (with pagination parameters)
        var mediaList = anilistService.fetchThisSeasonAnimes(request);

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
    public ResponseEntity<?> searchAnimes(@ModelAttribute SearchAnimesRequest request) {
        try {
            // Try to fetch searched animes from Anilist service (based on title) with pagination parameters
            var mediaList = anilistService.searchAnimes(request);

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
            @RequestParam(name = "seasonYear" , required = false) Integer seasonYear,
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
    public ResponseEntity<?> getGenres(@Valid @ModelAttribute GenresRequest request) {
        try {
            // Try to fetch the genres list from Anilist service
            var genres = anilistService.fetchGenres(request.getLang());

            // Return the fetched genres in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(genres);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the anime details by its ID
    @GetMapping
    public ResponseEntity<?> getAnimeDetailsById(@RequestParam(name = "id") Integer animeId) {
        try {
            // Try to fetch the anime details from Anilist service based on anime ID
            var anime = anilistService.fetchAnimeDetailsById(animeId);

            // Return the fetched genres in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(anime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the animes from for specific studio by its ID
    @GetMapping("/studio")
    public ResponseEntity<?> getStudioAnimes(@ModelAttribute StudioAnimesRequest request) {
        try {
            // Try to fetch the animes from Anilist service based on studio ID with pagination parameters
            var studioDetails = anilistService.fetchStudioAnimes(request.getStudioId());

            // Return the fetched animes in the response body with HTTP status 200 OK
            return ResponseEntity.ok().body(studioDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

