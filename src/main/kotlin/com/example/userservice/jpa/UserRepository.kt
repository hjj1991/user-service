package com.example.userservice.jpa

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserEntity, Long> {

    fun findByUserId(userId: String): UserEntity?
    fun findByEmail(username: String): UserEntity?
}