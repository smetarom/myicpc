package com.myicpc.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * @author Roman Smetana
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityAppConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(getUserQuery()).authoritiesByUsernameQuery(getAuthoritiesQuery())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/private/install/**").permitAll()
                    .anyRequest().authenticated().and()
                .formLogin().loginPage("/private/login").loginProcessingUrl("/private/login").defaultSuccessUrl("/private/home").failureUrl("/private/loginfailed").permitAll().and()
                .logout().logoutUrl("/private/logout").logoutSuccessUrl("/private/login");

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }

    protected String getUserQuery() {
        return "SELECT username, password, enabled FROM SystemUser WHERE username = ?";
    }

    protected String getAuthoritiesQuery() {
        return "SELECT u.username, ur.authority FROM SystemUser u, SystemUserRole ur WHERE u.id = ur.userId and u.username = ?";
    }
}
