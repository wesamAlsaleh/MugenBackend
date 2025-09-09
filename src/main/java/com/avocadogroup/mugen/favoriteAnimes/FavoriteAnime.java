package com.avocadogroup.mugen.favoriteAnimes;

import com.avocadogroup.mugen.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "favorites", schema = "mugen")
@Getter
@Setter
public class FavoriteAnime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "anime_id")
    private Long animeId;

    @Column(name = "created_at", insertable = false, updatable = false) // createdAt is set by the database
    private Instant createdAt;
}