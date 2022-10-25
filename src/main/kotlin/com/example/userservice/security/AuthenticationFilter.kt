package com.example.userservice.security

import com.example.userservice.service.UserService
import com.example.userservice.vo.RequestLogin
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val env: Environment,
    private val userService: UserService,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {


    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        val creds = ObjectMapper().readValue(request.inputStream, RequestLogin::class.java)

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(creds.email, creds.password, mutableListOf())
        )

    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val userName = (authResult.principal as User).username
        val userDetails = userService.getUserDetailsByEmail(userName) ?: throw UsernameNotFoundException(userName)

        val token = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(userDetails.userId)
            .setExpiration(
                Date(
                    System.currentTimeMillis() + (env.getProperty("token.expiration_time")?.toLong()
                        ?: throw IllegalArgumentException())
                )
            )
            .signWith(
                Keys.hmacShaKeyFor((env.getProperty("token.secret") as String).toByteArray(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS512
            )
            .compact()

        response.addHeader("token", token)
        response.addHeader("userId", userDetails.userId)
    }
}