package com.example.Hotel_Management_System.entity;



import com.example.Hotel_Management_System.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Email is required")
    private String email;       // User's email address (used as username)

    @NotBlank(message = "Name is required")
    private String name;       // User's full name

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;    // User's contact phone number
    private String password;       // Encrypted password for authentication
    private String role;           // User role (e.g., ADMIN, USER, STAFF)
    private List<Booking> bookings = new ArrayList<>();

    // ========== UserDetails Interface Implementation ==========

    /**
     * Returns the authorities granted to the user
     * Converts the user's role to a Spring Security GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    /**
     * Returns the username used to authenticate the user
     * In this implementation, email is used as the username
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired
     * Default implementation returns true (account never expires)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked
     * Default implementation returns true (account never locked)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired
     * Default implementation returns true (credentials never expire)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled
     * Default implementation returns true (account always enabled)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}