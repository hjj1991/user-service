package com.example.userservice.controller

import com.example.userservice.dto.UserDto
import com.example.userservice.service.UserService
import com.example.userservice.vo.Greeting
import com.example.userservice.vo.RequestUser
import com.example.userservice.vo.ResponseUser
import io.micrometer.core.annotation.Timed
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
    private val env: Environment,
    private val greeting: Greeting,
    private val userService: UserService,
) {

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    fun status(): String {
        return "It's Working in User Service" +
                ", PORT(local.server.port) = ${env.getProperty("local.server.port")}" +
                ", PORT(server.port) = ${env.getProperty("server.port")}" +
                ", token secret= ${env.getProperty("token.secret")}" +
                ", token expiration time = ${env.getProperty("token.expiration_time")}"
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    fun welcome(): String? {
//        return env.getProperty("greeting.message")
        return greeting.message
    }

    @PostMapping("/users")
    fun createUser(@RequestBody user: RequestUser): ResponseEntity<ResponseUser> {

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT
        val userDto = mapper.map(user, UserDto::class.java)
        userService.createUser(userDto)

        val responseUser = mapper.map(userDto, ResponseUser::class.java)

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser)

    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<ResponseUser>> {
        val userList = userService.getUserByAll()

        val result = mutableListOf<ResponseUser>()

        userList.forEach { userEntity -> result.add(ModelMapper().map(userEntity, ResponseUser::class.java)) }
        return ResponseEntity.status(HttpStatus.OK).body(result)

    }

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<*> {
        val userDto = userService.getUserByUserId(userId)

        val returnValue = ModelMapper().map(userDto, ResponseUser::class.java)

        return ResponseEntity.status(HttpStatus.OK).body(returnValue)
    }
}