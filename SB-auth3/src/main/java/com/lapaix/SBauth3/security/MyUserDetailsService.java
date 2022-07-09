package com.lapaix.SBauth3.security;

import com.lapaix.SBauth3.models.User;
import com.lapaix.SBauth3.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * By Gershom
 * UserDetailsService: to provide custom implementation to fetch the details of the user authenticated into the applilcation
 * loadByUsername: method provide by the above UserDetailsService interface to ahieve the above function
 * If no user found, "UsernameNotFoundException" is thrown
 *
 * */
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<User> userFound = userRepo.findByEmail(email);
        if(userFound.isEmpty()){
            throw new UsernameNotFoundException("No user with email "+email+" found");
        }

        User user = userFound.get();
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
