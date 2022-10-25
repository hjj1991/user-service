package com.example.userservice.service

import com.example.userservice.dto.UserDto
import com.example.userservice.jpa.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService: UserDetailsService {

    fun createUser(userDto: UserDto):UserDto?

    fun getUserByUserId(userId: String): UserDto?
    fun getUserByAll(): Iterable<UserEntity>
    fun getUserDetailsByEmail(userName: String): UserDto?
}