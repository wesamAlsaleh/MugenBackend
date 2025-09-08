package com.avocadogroup.mugen.anilist;

import com.avocadogroup.mugen.anilist.dtos.SearchAnimesRequest;
import com.avocadogroup.mugen.anilist.dtos.ThisSeasonAnimesRequest;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
