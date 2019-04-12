package com.example.studentlistview

object PasswordController {
    fun Check(username: String, password: String): Boolean {
        return password.startsWith("M")
    }
}
