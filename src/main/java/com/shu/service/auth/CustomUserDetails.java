package com.shu.service.auth;

import com.shu.model.entity.User;
import com.shu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


/**
 *
 * CUSTOM USER DETAILS SERVICE:
 * Implements Spring Security's UserDetailsService to load user-specific data.
 *
 * Responsibilities:
 * - Retrieve user information from the database by email.
 * - Convert the user's role into GrantedAuthority for Spring Security.
 * - Throw UsernameNotFoundException if the user does not exist.
 *
 * Used by Spring Security during authentication to verify credentials and assign roles.
 */
@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their username (email in this case).
     *
     * @param username the email of the user to authenticate
     * @return UserDetails object containing username, password, and authorities
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1️. Retrieve the user from the database using their email.
        //    We treat the email as the username for authentication purposes.
        User user = userRepository.findByEmail(username);

        // 2️. If no user is found, throw an exception to let Spring Security know authentication failed.
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 3️. Convert the user's role into a GrantedAuthority.
        //    Spring Security uses authorities to determine access to endpoints.
        //    Here, user.getRole() returns a UserRole enum, which we convert to a String.
        GrantedAuthority authority = new SimpleGrantedAuthority(
                user.getRole().toString()
        );

        // 4️. Wrap the authority in a collection (Spring Security expects a collection of authorities).
        Collection<GrantedAuthority> authorities =
                Collections.singletonList(authority);

        // 5️. Return a Spring Security User object containing:
        //    - username (email)
        //    - password (hashed)
        //    - authorities (roles/permissions)
        //    This object is then used by Spring Security to authenticate and authorize the user.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
