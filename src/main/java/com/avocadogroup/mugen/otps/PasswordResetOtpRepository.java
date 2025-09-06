package com.avocadogroup.mugen.otps;

import com.avocadogroup.mugen.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    // Custom query to find the latest OTP for a user
    @Query("SELECT o FROM PasswordResetOtp o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    Optional<PasswordResetOtp> findLatestByUserId(@Param("userId") Long userId);

    // Custom query to find the latest OTP for a user and fetch the user eagerly
    @Query("SELECT o FROM PasswordResetOtp o WHERE o.user.id = :userId ORDER BY o.createdAt DESC") // Get the latest OTP for a user
    @EntityGraph(attributePaths = "user") // Eagerly load the user relationship
    Optional<PasswordResetOtp> findLatestOtpByUserIdWithUser(@Param("userId") Long userId);

    // Custom query to delete all OTPs for a user
    void deleteAllByUser(User user);
}
