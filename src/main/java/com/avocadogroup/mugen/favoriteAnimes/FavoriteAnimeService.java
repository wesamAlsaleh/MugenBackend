package com.avocadogroup.mugen.favoriteAnimes;

import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FavoriteAnimeService {
    private final FavoriteAnimeRepository favoriteRepository;
    private final FavoriteAnimeMapper favoriteMapper;
    private final AuthenticationService authenticationService;

    // Function to add an anime to favorites table
    public void addFavoriteAnime(Long animeId) {
        // Get the current authenticated user
        var user = authenticationService.getCurrentUser();

        // Create a new Favorite entity and set the animeId and user fields
        var favoriteAnime = new FavoriteAnime();
        favoriteAnime.setAnimeId(animeId);
        favoriteAnime.setUser(user);

        // Save the favorite anime to the database
        favoriteRepository.save(favoriteAnime);
    }

    // Function to remove an anime from favorites table
    @Transactional // Ensures the operation is executed within a transaction
    public void removeFavoriteAnime(Long animeId) {
        // Get the current authenticated user
        var user = authenticationService.getCurrentUser();

        // Find the favorite anime entry by user and animeId and delete it
        favoriteRepository.deleteFavoriteAnimeByUserId(user.getId(), animeId);
    }

    // Function to get the ids of all favorite animes for the current user
    public List<Long> getFavoriteAnimes() {
        // Get the current authenticated user
        var user = authenticationService.getCurrentUser();

        // Retrieve all favorite animes for the user and map them to an array of animeIds
        return favoriteRepository.getFavoriteAnimesByUserId((user.getId()));
    }

    // Function to check if an anime is in the user's favorites
    public boolean isFavoriteAnime(Long animeId, Long userId) {
        // Return true if the entry exists, otherwise false
        return favoriteRepository.isFavoriteAnimeByAnimeId(userId, animeId)
                .isPresent();
    }
}
