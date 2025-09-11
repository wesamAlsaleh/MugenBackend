package com.avocadogroup.mugen.favoriteAnimes;

import com.avocadogroup.mugen.anilist.dtos.AnimeListRequest;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import com.avocadogroup.mugen.global.dtos.ErrorDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
@Tag(name = "Favorite Animes", description = "API endpoints for managing favorite animes")
public class FavoriteAnimeController {
    private final FavoriteAnimeService favoriteService;
    private final AnilistService anilistService;

    // API endpoint to add an anime to favorites using animeId as a request parameter
    @PostMapping("/add")
    public ResponseEntity<?> addFavoriteAnime(
            @RequestParam(name = "id") Long animeId
    ) {
        // Try to add the favorite anime using the service
        try {
            // Call the service method to add the favorite anime
            favoriteService.addFavoriteAnime(animeId);

            // Return a success response
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to remove an anime from favorites using animeId as a request parameter
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFavoriteAnime(
            @RequestParam(name = "id") Long animeId
    ) {
        // Try to add the favorite anime using the service
        try {
            // Call the service method to remove the favorite anime
            favoriteService.removeFavoriteAnime(animeId);

            // Return a success response
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the ids of all favorite animes for the current user
    @GetMapping("/list")
    public ResponseEntity<?> getFavoriteAnimes(
            @RequestParam(defaultValue = "9", required = false) int perPage,
            @RequestParam(defaultValue = "1", required = false) int page
    ) {
        // Try to get the favorite animes using the service
        try {
            // Get the list of favorite anime IDs from the database
            var animesIds = favoriteService.getFavoriteAnimes();

            // Call the anilist APO to get the anime details using the retrieved ids
            var data = anilistService.fetchAnimesByIds(new AnimeListRequest(page, perPage, animesIds));

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
