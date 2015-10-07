package com.myicpc.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security configuration and its integration with Spring MVC
 *
 * @author Roman Smetana
 */
@Configuration
@EnableWebSecurity
public class SecurityAppConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private DataSource dataSource;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * Defines the logic to handle user login
     *
     * @param auth authentication builder
     * @throws Exception user login failed
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Defines security URLs and role based access
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/private/install/**").permitAll()
                .antMatchers("/private/getting-started/**").hasRole("ADMIN")
                .antMatchers("/private/settings/**").hasRole("ADMIN")
                .antMatchers("/private/users/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        // form logout
        http.logout()
                .logoutUrl("/private/logout")
                .logoutSuccessUrl("/private/login");

        // form login
        http.formLogin()
                .loginPage("/private/login")
                .loginProcessingUrl("/private/login")
                .defaultSuccessUrl("/private/home")
                .failureUrl("/private/loginfailed")
                .permitAll();

        http.exceptionHandling().accessDeniedPage("/private/access-denied");
    }

    /**
     * Defines the hash algorithm to protect user passwords
     *
     * @return hash encoder
     * @throws Exception hash encoder creation failed
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }
}
