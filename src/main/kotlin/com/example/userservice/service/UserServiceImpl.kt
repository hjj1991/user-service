package com.example.userservice.service

import com.example.userservice.client.OrderServiceClient
import com.example.userservice.dto.UserDto
import com.example.userservice.jpa.UserEntity
import com.example.userservice.jpa.UserRepository
import com.example.userservice.vo.ResponseOrder
import feign.FeignException
import org.springframework.stereotype.Service
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val restTemplate: RestTemplate,
    private val env: Environment,
    private val orderServiceClient: OrderServiceClient,
): UserService {
    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    override fun createUser(userDto: UserDto): UserDto? {
        userDto.userId = UUID.randomUUID().toString()

        val modelMapper = ModelMapper()
        modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT
        val userEntity = modelMapper.map(userDto, UserEntity::class.java)
        userEntity.encryptedPwd = passwordEncoder.encode(userDto.pwd)

        userRepository.save(userEntity)

        return modelMapper.map(userEntity, UserDto::class.java)
    }

    override fun getUserByUserId(userId: String): UserDto {
        val userEntity = userRepository.findByUserId(userId)?: throw UsernameNotFoundException("User not found")

        val userDto = ModelMapper().map(userEntity, UserDto::class.java)

        /* Using as rest template */
//        val orderUrl = String.format(env.getProperty("order_service.url") as String, userId)
//        val orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null, object :
//            ParameterizedTypeReference<List<ResponseOrder>>() {})
//        val orderList = orderListResponse.body

        /* Using as feign client */
        /* Feign Exception Handling */
//        var orderList = listOf<ResponseOrder>()
//        kotlin.runCatching {
//            orderList = orderServiceClient.getOrders(userId)
//        }.onFailure {
//            when(it){
//                is FeignException -> log.error(it.message)
//            }
//        }
        val orderList = orderServiceClient.getOrders(userId)

        userDto.orders = orderList


        return userDto

    }

    override fun getUserByAll(): Iterable<UserEntity> {
        return userRepository.findAll()
    }

    override fun getUserDetailsByEmail(email: String): UserDto? {
        val userEntity = userRepository.findByEmail(email) ?: throw UsernameNotFoundException(email)

        return ModelMapper().map(userEntity, UserDto::class.java)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username)?: throw UsernameNotFoundException(username)

        return User(userEntity.email, userEntity.encryptedPwd, true, true, true, true, mutableListOf())
    }
}