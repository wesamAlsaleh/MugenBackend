package com.avocadogroup.mugen.userAnimeList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnimeProgressRepository extends JpaRepository<UserAnimeProgress, Long> {
    // Custom query to check if a UserAnimeProgress entry exists for a given user and animeId
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    // Custom query to delete a UserAnimeProgress entry for a given user and animeId
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}
