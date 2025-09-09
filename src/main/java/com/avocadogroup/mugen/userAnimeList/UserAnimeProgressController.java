package com.avocadogroup.mugen.userAnimeList;

import com.avocadogroup.mugen.userAnimeList.dtos.AddAnimeToListRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
@AllArgsConstructor
public class UserAnimeProgressController {
    private final UserAnimeProgressService userAnimeService;

    // API endpoint to add an anime to user's anime list
    @PostMapping("/add")
    public ResponseEntity<?> addAnimeToList(
            @Valid @RequestBody AddAnimeToListRequest request
    ) {
        try {
        // Call the service method to add the anime to user's list
         userAnimeService.addAnimeToList(request);

        // Implementation to add anime to user's list
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
