package com.avocadogroup.mugen.favoriteAnimes;

import com.avocadogroup.mugen.anilist.dtos.FavoriteAnimesRequest;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import com.avocadogroup.mugen.global.dtos.ErrorDto;
import com.avocadogroup.mugen.favoriteAnimes.dtos.AddFavoriteAnimeRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
public class FavoriteAnimeController {
    private final FavoriteAnimeService favoriteService;
    private final AnilistService anilistService;

    // API endpoint to add an anime to favorites using animeId as a request parameter
    @PostMapping("/add")
    public ResponseEntity<?> addFavoriteAnime(@Valid @ModelAttribute AddFavoriteAnimeRequest request) {
        // Try to add the favorite anime using the service
        try {
            // Call the service method to add the favorite anime
            favoriteService.addFavoriteAnime(request.getAnimeId());

            // Return a success response
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to remove an anime from favorites using animeId as a request parameter
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFavoriteAnime(@Valid @ModelAttribute AddFavoriteAnimeRequest request) {
        // Try to add the favorite anime using the service
        try {
            // Call the service method to remove the favorite anime
            favoriteService.removeFavoriteAnime(request.getAnimeId());

            // Return a success response
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the ids of all favorite animes for the current user
    @GetMapping("/list")
    public ResponseEntity<?> getFavoriteAnimes(@RequestParam(required = false, defaultValue = "6") int perPage) {
        // Try to get the favorite animes using the service
        try {
            // Call the favorite service method to get the favorite animes ids
            var animesIds = favoriteService.getFavoriteAnimes();

            // Call the anilist service method to get the anime details based on the ids
            var data = anilistService.fetchAnimesByIds(new FavoriteAnimesRequest(perPage, animesIds));

            // Return the list of favorite anime IDs
            return ResponseEntity.ok().body(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to handle duplicate favorite anime addition attempts (not in global exception handler to provide specific message)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDuplicateFavoriteException() {
        return ResponseEntity.badRequest().body(new ErrorDto("This anime is already in your favorites."));
    }
}
