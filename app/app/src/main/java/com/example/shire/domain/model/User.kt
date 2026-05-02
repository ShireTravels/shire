package com.example.shire.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val login: String = "",
    val username: String = "",
    val birthdate: String = "",
    val address: String = "",
    val country: String = "",
    val phone: String = "",
    val receiveEmails: Boolean = false
)