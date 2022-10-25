package com.example.userservice.vo

import javax.validation.constraints.Email
import javax.validation.constraints.Size

class RequestUser(
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    var email: String,
    @Size(min = 2, message = "Name not be less than two characters")
    var name: String,
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    var pwd: String,

) {
}