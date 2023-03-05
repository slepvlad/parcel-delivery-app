package com.guavapay.parceldeliveryapp.repository;

import com.guavapay.parceldeliveryapp.model.Role;
import com.guavapay.parceldeliveryapp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph("fullUserEntity")
    Optional<User> findByEmail(String email);

    List<User> findAllByRolesIn(Set<Role> roles);
}
