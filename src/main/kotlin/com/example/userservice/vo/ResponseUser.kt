package com.example.userservice.vo

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseUser {
    var email: String? = null
    var name: String? = null
    var userId: String? = null

    var orders: List<ResponseOrder>? = mutableListOf()
}
