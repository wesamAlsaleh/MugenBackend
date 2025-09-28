package com.avocadogroup.mugen.favoriteAnimes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteAnimeRepository extends JpaRepository<FavoriteAnime, Long> {
    // Custom query method to find a favorite anime by user and animeId and delete it
    @Modifying // Marks the query as a modifying query (INSERT, UPDATE, DELETE)
    @Query("DELETE FROM FavoriteAnime f WHERE f.user.id = :userId AND f.animeId = :animeId ")
    void deleteFavoriteAnimeByUserId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    // Custom query method to fetch the ids of all favorite animes for a specific user
    @Query("SELECT f.animeId FROM FavoriteAnime f WHERE f.user.id = :userId")
    List<Long> getFavoriteAnimesByUserId(@Param("userId") Long userId);

    // Custom query method to check if a favorite anime exists for a specific user and animeId
    @Query("SELECT a FROM FavoriteAnime a WHERE a.animeId = :animeId AND a.user.id = :userId")
    Optional<FavoriteAnime> isFavoriteAnimeByAnimeId(@Param("userId")Long userId, @Param("animeId") Long animeId);
}
