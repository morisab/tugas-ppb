package com.example.starbucks.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.starbucks.data.model.User
import com.example.starbucks.data.model.UserProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUserStorage @Inject constructor(
    private val context: Context
) {
    private val gson = Gson()

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_USERS = "users"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
    }

    fun saveUser(user: User) {
        val users = getAllUsers().toMutableMap()
        users[user.id] = user
        saveAllUsers(users)
    }

    fun getUserByEmail(email: String): User? {
        return getAllUsers().values.find { it.email == email }
    }

    fun getUserById(id: String): User? {
        return getAllUsers()[id]
    }

    fun setCurrentUserId(userId: String?) {
        sharedPreferences.edit()
            .putString(KEY_CURRENT_USER_ID, userId)
            .apply()
    }

    fun getCurrentUserId(): String? {
        return sharedPreferences.getString(KEY_CURRENT_USER_ID, null)
    }

    fun getCurrentUser(): User? {
        val userId = getCurrentUserId()
        return if (userId != null) getUserById(userId) else null
    }

    private fun getAllUsers(): Map<String, User> {
        val usersJson = sharedPreferences.getString(KEY_USERS, null)
        return if (usersJson != null) {
            val type = object : TypeToken<Map<String, User>>() {}.type
            gson.fromJson(usersJson, type)
        } else {
            emptyMap()
        }
    }

    private fun saveAllUsers(users: Map<String, User>) {
        val usersJson = gson.toJson(users)
        sharedPreferences.edit()
            .putString(KEY_USERS, usersJson)
            .apply()
    }

    fun clearCurrentUser() {
        setCurrentUserId(null)
    }
}