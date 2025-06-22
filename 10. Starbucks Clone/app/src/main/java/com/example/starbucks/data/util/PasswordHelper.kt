package com.example.starbucks.data.util

import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordHelper @Inject constructor() {

    fun hashPassword(password: String, salt: String = generateSalt()): String {
        val saltedPassword = password + salt
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(saltedPassword.toByteArray())
        return salt + ":" + hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val parts = hashedPassword.split(":")
        if (parts.size != 2) return false

        val salt = parts[0]
        val hash = parts[1]

        val newHash = hashPassword(password, salt).split(":")[1]
        return hash == newHash
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt.joinToString("") { "%02x".format(it) }
    }
}