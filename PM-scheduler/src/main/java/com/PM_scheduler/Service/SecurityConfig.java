package com.PM_scheduler.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/Supervisor/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/Supervisor/login")       // page that shows login form
                .loginProcessingUrl("/Supervisor/login") // <── authentication URL
                .defaultSuccessUrl("http://localhost:3000/PM-Scheduler/?role=Supervisor", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/Supervisor/login")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("sup1").password("{noop}pass1").roles("Supervisor").build(),
            User.withUsername("sup2").password("{noop}pass2").roles("Supervisor").build(),
            User.withUsername("sup3").password("{noop}pass3").roles("Supervisor").build(),
            User.withUsername("sup4").password("{noop}pass4").roles("Supervisor").build()
        );
    }
}
