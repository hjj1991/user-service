package com.example.userservice.error

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.lang.Exception

@Component
class FeignErrorDecoder(
    private val env: Environment,
): ErrorDecoder {

    override fun decode(methodKey: String, response: Response): Exception? {
        when(response.status()){
            404 -> {
                if(methodKey.contains("getOrders")){
                    return ResponseStatusException(HttpStatus.valueOf(response.status()), env.getProperty("order_service.exception.order_is_empty"))
                }
            }
            else -> return Exception(response.reason())
        }

        return null
    }
}