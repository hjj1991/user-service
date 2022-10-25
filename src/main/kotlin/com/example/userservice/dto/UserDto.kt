package com.example.userservice.dto

import com.example.userservice.vo.ResponseOrder
import java.util.Date

class UserDto(
) {
    var email: String? = null
    var name: String? = null
    var pwd: String? = null
    var userId: String? = null
    var createdAt: Date? = null

    var encryptedPwd: String? = null

    var orders: List<ResponseOrder>? = listOf()
}