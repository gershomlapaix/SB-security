package com.lapaix.SBauth3.controllers;

import com.lapaix.SBauth3.models.LoginCredentials;
import com.lapaix.SBauth3.models.User;
import com.lapaix.SBauth3.repository.UserRepo;
import com.lapaix.SBauth3.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user){
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user = userRepo.save(user);

        String token= jwtUtil.generateToken(user.getEmail());
        System.out.println(Collections.singletonMap("jwt-token", token));
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials credentials){
        try{
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
            authManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(credentials.getEmail());
            return Collections.singletonMap("jwt-token", token);
        }catch (AuthenticationException authexc){
            throw new RuntimeException("Invalid login credentials");
        }
    }
}
