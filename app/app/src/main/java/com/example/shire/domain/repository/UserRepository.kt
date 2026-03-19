package com.example.shire.domain.repository

import com.example.shire.domain.model.User

interface UserRepository {
    fun getUser(userId: Int): User
    fun getUsers(): List<User>
    fun addUser(user: User): User
    fun deleteUser(userId: Int): Boolean
    fun updateUser(user: User): Boolean
}