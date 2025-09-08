package com.avocadogroup.mugen.watchList.favoriteAnimes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteAnimeRepository extends JpaRepository<FavoriteAnime, Long> {
    // Custom query method to find a favorite anime by user and animeId and delete it
    @Modifying // Marks the query as a modifying query (INSERT, UPDATE, DELETE)
    @Query("DELETE FROM FavoriteAnime f WHERE f.user.id = :userId AND f.animeId = :animeId ")
    void deleteFavoriteAnimeByUserId(@Param("userId") Long userId, @Param("animeId") Long animeId);
}
