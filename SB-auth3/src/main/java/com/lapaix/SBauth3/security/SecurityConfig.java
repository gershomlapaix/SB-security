package com.lapaix.SBauth3.security;

import com.lapaix.SBauth3.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * By Gershom
 *                Some of the important notes
 *   1. The "auth" routes can be accessed by all users since register and login is for everyone
 *   2. The "user" routes can be accessed by only authenticated users with the role "USER" which is set by MyUserDetailsService
 *   3. Server will reject the requests as unauthorized when entry point is reached
 *   4. JWTFilter is added to the filter chain in order to process the incoming requests
 *   5. Creating a bean for password encoder
 *   6. Exposing the bean of the authentication manager which will be used to run the authentication process in the "authController"
 * */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/user/**").hasRole("USER")
                .and()
                .userDetailsService(userDetailsService)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }
}
