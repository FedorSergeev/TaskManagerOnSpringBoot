package com.project.repository;

import com.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer>, JpaRepository<User, Integer> {

    User findByFirstName(String firstName);
    Optional<User> findById(Integer id);
}
