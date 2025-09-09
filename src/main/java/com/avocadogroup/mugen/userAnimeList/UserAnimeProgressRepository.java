package com.avocadogroup.mugen.userAnimeList;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAnimeProgressRepository extends JpaRepository<UserAnimeProgress, Long> {
    // Custom query to check if a UserAnimeProgress entry exists for a given user and animeId
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    // Custom query to delete a UserAnimeProgress entry for a given user and animeId
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);

    // Custom query to get the user list based on userId and status
    @Query("SELECT a.animeId FROM UserAnimeProgress a WHERE a.user.id = :userId AND a.status = :status")
    List<Long> getUserListIdsByStatus(@Param("userId") Long userId, @Param("status") String status);

    // Custom query to get the user details in with their anime list
    @Query("SELECT a FROM UserAnimeProgress a WHERE a.user.id = :userId")
    @EntityGraph(attributePaths = "user") // Eagerly fetch the associated user entity
    Object getUserList(@Param("userId") Long userId);
}
