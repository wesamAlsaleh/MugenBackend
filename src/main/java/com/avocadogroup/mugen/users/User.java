package com.avocadogroup.mugen.users;

import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import com.avocadogroup.mugen.users.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "users", schema = "mugen")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    private UserRole role;

    @Column(name = "preferred_language")
    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    private UserPreferredLanguage preferredLanguage;

    @Column(name = "created_at", insertable = false, updatable = false) // Automatically set by the database
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // Automatically set by the database
    private LocalDateTime updatedAt;

}