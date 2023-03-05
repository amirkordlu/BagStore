package com.amk.market.model.repository.user

import android.content.SharedPreferences
import com.amk.market.model.net.ApiService
import com.amk.market.model.repository.TokenInMemory
import com.amk.market.util.VALUE_SUCCESS
import com.google.gson.JsonObject

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
) : UserRepository {

    override suspend fun signUp(name: String, username: String, password: String): String {
        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", username)
            addProperty("password", password)
        }
        val result = apiService.signUp(jsonObject)
        return if (result.success) {
            TokenInMemory.refreshToken(username, result.token)
            saveToken(result.token)
            saveUsername(username)
            saveUserLoginTime()
            VALUE_SUCCESS
        } else {
            result.message
        }
    }

    override suspend fun signIn(username: String, password: String): String {
        val jsonObject = JsonObject().apply {
            addProperty("email", username)
            addProperty("password", password)
        }
        val result = apiService.signIn(jsonObject)
        return if (result.success) {
            TokenInMemory.refreshToken(username, result.token)
            saveToken(result.token)
            saveUsername(username)
            saveUserLoginTime()
            VALUE_SUCCESS
        } else {
            result.message
        }
    }

    override fun signOut() {
        TokenInMemory.refreshToken(null, null)
        sharedPref.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(), getToken())
    }

    override fun saveToken(newToken: String) {
        sharedPref.edit().putString("token", newToken).apply()
    }

    override fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

    override fun saveUsername(username: String) {
        sharedPref.edit().putString("username", username).apply()
    }

    override fun getUserName(): String? {
        return sharedPref.getString("username", null)
    }

    override fun saveUserLocation(address: String, postalCode: String) {
        sharedPref.edit().putString("address", address).apply()
        sharedPref.edit().putString("postalCode", postalCode).apply()
    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sharedPref.getString("address", "click to add")!!
        val postalCode = sharedPref.getString("postalCode", "click to add")!!
        return Pair(address, postalCode)
    }

    override fun saveUserLoginTime() {
        val now = System.currentTimeMillis()
        sharedPref.edit().putString("login_time", now.toString()).apply()
    }

    override fun getUserLoginTime(): String {
        return sharedPref.getString("login_time", "0")!!
    }

}