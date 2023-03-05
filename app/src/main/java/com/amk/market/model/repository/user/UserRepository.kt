package com.amk.market.model.repository.user

interface UserRepository {

    // Online -->
    suspend fun signUp(name: String, username: String, password: String): String
    suspend fun signIn(username: String, password: String): String

    // Offline -->
    fun signOut()

    // read token from shared preferences
    fun loadToken()

    fun saveToken(newToken: String)
    fun getToken(): String?

    fun saveUsername(username: String)
    fun getUserName(): String?

    fun saveUserLocation(address: String, postalCode: String)
    fun getUserLocation(): Pair<String, String>

    fun saveUserLoginTime()
    fun getUserLoginTime():String
}