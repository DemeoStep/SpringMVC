package com.oched.booksprj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login, password, auth FROM users WHERE login=?")
                .authoritiesByUsernameQuery(
                        "SELECT user_id, roles FROM user_roles WHERE user_id=(SELECT id from users WHERE login=?)"
                ).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // passwordEncoder.encode("12345"))
        // passwordEncoder.matches(passwordFromRequest, encodedPasswordFromDB)

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/users/add", "/users/all", "/users/edit", "/books/add", "/books/edit", "/books/delete")
                .hasAnyRole("ADMIN", "SUPER")
                .mvcMatchers("/hello/**")
                .hasAnyRole("ADMIN", "USER", "SUPER")
                .antMatchers(
                        "/", "/login", "/registration", "/perform_logout", "/books/all"
                ).permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/perform_login")
                .failureForwardUrl("/login?error=true")
                .defaultSuccessUrl("/",false)
                .and()
                .logout().logoutUrl("/perform_logout").logoutSuccessUrl("/");
    }

    @Override
    public final void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers("/swagger/ui/**", "/v3/api-docs/**");
    }
}

