package com.example.userservice.jpa

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false, length = 50, unique = true)
    var email: String,
    @Column(nullable = false, length = 50)
    var name: String,
    @Column(nullable = false, unique = true)
    var userId: String,
    @Column(nullable = false, unique = true)
    var encryptedPwd: String? = null,
) {
}