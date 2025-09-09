package com.avocadogroup.mugen.userAnimeList;

import com.avocadogroup.mugen.anilist.dtos.AnimeDto;
import com.avocadogroup.mugen.anilist.dtos.AnimeListRequest;
import com.avocadogroup.mugen.anilist.dtos.AnimeListResponse;
import com.avocadogroup.mugen.anilist.services.AnilistService;
import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import com.avocadogroup.mugen.userAnimeList.dtos.AddAnimeToListRequest;
import com.avocadogroup.mugen.userAnimeList.dtos.UserListRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAnimeProgressService {
    private final AuthenticationService authenticationService;
    private final UserAnimeProgressMapper userAnimeProgressMapper;
    private final UserAnimeProgressRepository userAnimeProgressRepository;

    // Function to add an anime to user anime list table
    @Transactional
    public void addAnimeToList(AddAnimeToListRequest request) {
        // Get the current authenticated user
        var user = authenticationService.getCurrentUser();

        // Check if the anime is already in the user's list
        var existingEntry = userAnimeProgressRepository.existsByUserIdAndAnimeId(user.getId(), request.getAnimeId());

        // If it exists, remove it from the list
        if (existingEntry) {
            // Remove the existing entry from the database
            userAnimeProgressRepository.deleteByUserIdAndAnimeId(user.getId(), request.getAnimeId());

            // Exit the method
            return;
        }

        // Create a new list entry and set the animeId, status, and user fields
        var userAnimeProgress = userAnimeProgressMapper.toEntity(request);
        userAnimeProgress.setUser(user);

        // Save the user anime progress to the database
        userAnimeProgressRepository.save(userAnimeProgress);
    }

    // Function to get the user list based on status
    public List<Long> getUserListIds(UserListRequest request) {
        // Get the current authenticated user
        var user = authenticationService.getCurrentUser();

        // Retrieve the user's anime list based on the status and user ID
        return userAnimeProgressRepository.getUserListIdsByStatus(user.getId(), request.getStatus().toString());
    }
}
