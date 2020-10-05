package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsernameOrEmail(String username, String email);
    Optional<Users> findByUsernameOrEmailOrMobilePhoneNumber(String username, String email, String mobilePhoneNumber);
    Optional<Users> findByUsername(String username);
}
