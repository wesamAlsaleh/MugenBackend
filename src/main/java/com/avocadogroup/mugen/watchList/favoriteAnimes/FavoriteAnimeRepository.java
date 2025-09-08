package com.avocadogroup.mugen.watchList.favoriteAnimes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface FavoriteAnimeRepository extends JpaRepository<FavoriteAnime, Long> {
    // Custom query method to find a favorite anime by user and animeId and delete it
    @Modifying // Marks the query as a modifying query (INSERT, UPDATE, DELETE)
    @Query("DELETE FROM FavoriteAnime f WHERE f.user.id = :userId AND f.animeId = :animeId ")
    void deleteFavoriteAnimeByUserId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    // Custom query method to fetch the ids of all favorite animes for a specific user
    @Query("SELECT f.animeId FROM FavoriteAnime f WHERE f.user.id = :userId")
    List<Long> getFavoriteAnimesByUserId(@Param("userId") Long userId);
}
