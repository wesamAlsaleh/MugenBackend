package com.avocadogroup.mugen.otps;

import com.avocadogroup.mugen.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    // Custom query to find the latest OTP for a user
    @Query("SELECT o FROM PasswordResetOtp o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    Optional<PasswordResetOtp> findLatestByUserId(@Param("userId") Long userId);

    // Custom query to find the user according to the last otp code
    @Query("SELECT o.user FROM PasswordResetOtp o WHERE o.otpCode = :otpCode ORDER BY o.createdAt DESC")
    Optional<User> findUserByOtpCode(@Param("otpCode") String otpCode);

}
