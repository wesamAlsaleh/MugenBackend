package com.avocadogroup.mugen.userAnimeList;

import com.avocadogroup.mugen.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_animes_list", schema = "mugen")
@Getter
@Setter
public class UserAnimeProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "anime_id")
    private Long animeId;

    @Column(name = "status")
    private String status;

    @Column(name = "progress", insertable = false, updatable = false) // progress is set to 0 by the database
    private Integer progress;

    @Column(name = "score")
    private Integer score;

    @Column(name = "started_at", insertable = false, updatable = false) // startedAt is set by the database
    private Instant startedAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // updatedAt is set by the database
    private Instant updatedAt;

}