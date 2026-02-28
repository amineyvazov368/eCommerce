package org.example.ecommers.repository;

import org.example.ecommers.dto.CartDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    Optional<Cart> findByUser(User user);


}
