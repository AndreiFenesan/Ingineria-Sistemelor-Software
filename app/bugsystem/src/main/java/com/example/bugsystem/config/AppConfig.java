package com.example.bugsystem.config;

import com.example.bugsystem.model.Programmer;
import com.example.bugsystem.model.Tester;
import com.example.bugsystem.repositories.IProgrammerRepository;
import com.example.bugsystem.repositories.ITesterRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Configuration
public class AppConfig {
    private final ITesterRepository testerRepository;
    private final IProgrammerRepository programmerRepository;

    public AppConfig(ITesterRepository testerRepository, IProgrammerRepository programmerRepository) {
        this.testerRepository = testerRepository;
        this.programmerRepository = programmerRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<Tester> optionalTester = testerRepository.findTesterByUsername(username);
                if (optionalTester.isPresent()) {
                    return optionalTester.get();
                }
                Optional<Programmer> optionalProgrammer = programmerRepository.findProgrammerByUsername(username);
                System.out.println(optionalProgrammer.isPresent());
                if (optionalProgrammer.isPresent()) {
                    return optionalProgrammer.get();
                }
                throw new UsernameNotFoundException("User not found");
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
