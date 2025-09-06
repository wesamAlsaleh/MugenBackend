package com.avocadogroup.mugen.otps;

import com.avocadogroup.mugen.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "password_reset_otps", schema = "mugen")
@Getter
@Setter
public class PasswordResetOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id") // Foreign key column
    private User user;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "expiry")
    private Instant expiry;

    @Column(name = "used")
    private Boolean used = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", insertable = false, updatable = false) // createdAt is set by the database
    private Instant createdAt;

    // Method to check if the OTP is expired (after 15 minutes)
    public boolean isExpired() {
        // Return true if the current time is after the expiry time
        return Instant.now().isAfter(this.expiry);
    }

    // Method to check if the OTP is used
    public boolean isUsed() {
        return this.used;
    }

    // Method to mark the OTP as used
    public void markAsUsed() {
        this.used = true;
    }
}