package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository  extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByDeviceID(String deviceID);
    Optional<OTP> findByOtpCode(String otpCode);
    Optional<OTP> findByUserID(Integer UserID);
    Optional<OTP> findByUsernameOrEmailOrMobilePhoneNumber(String username, String email, String mobilePhoneNumber);
    Optional<OTP> findByEmail(String email);

}
