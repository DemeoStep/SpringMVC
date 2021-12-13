package com.oched.booksprj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
}

/*

<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns:beans="http://www.springframework.org/schema/beans"
             xmlns="http://www.springframework.org/schema/security"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
                http://www.springframework.org/schema/security
                http://www.springframework.org/schema/security/spring-security-5.3.xsd">

    <global-method-security pre-post-annotations="enabled"/>
    <http auto-config="true" use-expressions="true">
        <csrf disabled="true"/>

        <intercept-url pattern="/books/**" access="hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')"/>
        <intercept-url pattern="/users/**" access="hasAuthority('ROLE_ADMIN')"/>
        <intercept-url pattern="/rest/**"  access="hasAuthority('ROLE_ADMIN')"/>
        <intercept-url pattern="/*" access="permitAll"/>

        <form-login
                login-page="/login"
                username-parameter="username"
                password-parameter="password"
                login-processing-url="/perform_login"
                always-use-default-target="false"
                authentication-failure-url="/login?error=true"/>
        <logout
                logout-url="/perform_logout"
                logout-success-url="/"/>
    </http>


    <beans:bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
        <beans:property name="driverClass" value="com.mysql.cj.jdbc.Driver"/>
        <beans:property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
        <beans:property name="username" value="root"/>
        <beans:property name="password" value="11235813"/>
    </beans:bean>

    <beans:bean id="passwordEncoder" class="org.springframework.security.crypto.password.NoOpPasswordEncoder"/>

<!--        select username,password,enabled from users where username = ?-->
<!--        select username,authority from authorities where username = ?-->
        <authentication-manager>
            <authentication-provider>
                <jdbc-user-service
                        data-source-ref="dataSource"
                        users-by-username-query="SELECT login, password, id FROM users WHERE login=?"
                        authorities-by-username-query="SELECT user_id, roles FROM user_roles WHERE user_id=(SELECT id from users WHERE login=?)"
                />
                <password-encoder ref="passwordEncoder"/>
            </authentication-provider>
        </authentication-manager>
</beans:beans>

*/