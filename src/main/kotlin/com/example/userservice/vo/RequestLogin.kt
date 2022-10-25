package com.example.userservice.vo

import javax.validation.constraints.Size

class RequestLogin(

) {

    @Size(min = 2, message = "Email not b e less than two characters")
    var email: String? = null

    @Size(min = 8, message = "Password must be equals or grater than 8 characters")
    var password: String? = null
}