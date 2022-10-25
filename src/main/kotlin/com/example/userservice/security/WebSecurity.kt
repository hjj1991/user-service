package com.example.userservice.security

import com.example.userservice.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class WebSecurity(
    private val env: Environment,
    private val userService: UserService,
) {

    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {

        http.csrf().disable()
//            .authorizeRequests().antMatchers("/users/**").permitAll()

        http.authorizeRequests()
            .antMatchers("/error/**").permitAll()
            .antMatchers("/**").access("hasIpAddress('127.0.0.1')")
            .and()
            .addFilter(AuthenticationFilter(authenticationManager, env, userService))

        http.headers().frameOptions().disable()

        return http.build()
    }


}