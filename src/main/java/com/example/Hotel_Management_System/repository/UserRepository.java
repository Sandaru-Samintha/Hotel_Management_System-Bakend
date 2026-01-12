package com.example.Hotel_Management_System.repository;

import com.example.Hotel_Management_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    /**
     * Checks whether a User exists with the given email.
     *
     * Spring Data JPA automatically generates the query
     * based on the method name.
     *
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);


    /**
     * Finds a User by their email address.
     *
     * The result is wrapped in an Optional to avoid NullPointerException
     * if no user is found.
     *
     * @param email the email to search for
     * @return Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);
}
