package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("select u from Users u where (u.username = ?1 or u.email=?2) and u.status = ?3")
    Optional<Users> findByUsernameOrEmailAndStatus(String username, String email, Integer status);

    @Query("select u from Users u where u.username = ?1 and u.status = ?2")
    Optional<Users> findByUsernameAndStatus(String username, Integer status);

    @Query("select u from Users u where u.id = ?1 and u.status = ?2")
    Optional<Users> findByIdAndStatus(Integer id, Integer status);

    Optional<Users> findFirstByKtpUrlPathAndStatusIsNot(String name, Integer status);

    Optional<Users> findFirstByProfileUrlPathAndStatusIsNot(String name, Integer status);

    Optional<Users> findFirstByUserIDGoogleAndStatus(String userIdGoogle, Integer status);

}
