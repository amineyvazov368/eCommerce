package org.example.ecommers.repository;

import org.example.ecommers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);

    boolean existsByUserName(String username);
    boolean existsByEmail(String email);

}
