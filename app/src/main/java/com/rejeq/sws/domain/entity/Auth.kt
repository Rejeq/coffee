package com.rejeq.sws.domain.entity

data class LoginAuth(val login: String, val password: String)

data class RegisterAuth(val login: String, val password: String)

enum class UserAuthState {
    Logged,
    NotLogged,
    NotRegistered,
}
