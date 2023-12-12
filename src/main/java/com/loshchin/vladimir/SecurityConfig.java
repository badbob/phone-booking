package com.loshchin.vladimir;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/device/*/book").hasRole("USER")
            .antMatchers("/device/*/release").hasRole("USER")
            .and().csrf().disable()
            .authorizeRequests()
            .and()
            .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails bob =
            User.withDefaultPasswordEncoder()
            .username("bob")
            .password("bob")
            .roles("USER")
            .build();

        UserDetails alice =
            User.withDefaultPasswordEncoder()
            .username("alice")
            .password("alice")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(bob, alice);
    }

}
