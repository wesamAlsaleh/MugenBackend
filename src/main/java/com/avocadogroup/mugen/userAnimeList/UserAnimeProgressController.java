package com.avocadogroup.mugen.userAnimeList;

import com.avocadogroup.mugen.anilist.dtos.AnimeListRequest;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import com.avocadogroup.mugen.userAnimeList.dtos.AddAnimeToListRequest;
import com.avocadogroup.mugen.userAnimeList.dtos.UserListRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list")
@AllArgsConstructor
public class UserAnimeProgressController {
    private final UserAnimeProgressService userAnimeService;
    private final AnilistService anilistService;

    // API endpoint to add an anime to user's anime list
    @PostMapping("/add")
    public ResponseEntity<?> addAnimeToList(
            @RequestParam(name = "id") Long animeId,
            @RequestParam(name = "status") AnimeStatus status
    ) {
        try {
        // Call the service method to add the anime to user's list
         userAnimeService.addAnimeToList(new AddAnimeToListRequest(animeId, status));

        // Implementation to add anime to user's list
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // API endpoint to get the user list based on status from the request parameter
    @GetMapping
    public ResponseEntity<?> getUserList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "status", defaultValue = "WATCHING") AnimeStatus status
    ) {
        try {
            // Get the user list ids from the db
            var ids = userAnimeService.getUserListIds(new UserListRequest(page, perPage, status));

            // Call the Anilist API to get the anime details using the retrieved ids
            var data = anilistService.fetchAnimesByIds(new AnimeListRequest(page, perPage, ids));

            // Return the response with the user's anime list
            return ResponseEntity.ok().body(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
