package com.amk.market.model.net

import com.amk.market.model.data.LoginResponse
import com.amk.market.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthChecker : Authenticator, KoinComponent {
    private val apiService: ApiService by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (
            TokenInMemory.token != null
            && !response.request.url.pathSegments.last().equals("refreshToken", false)
        ) {
            val result = refreshToken()
            if (result) {
                return response.request
            }
        }
        return null
    }

    private fun refreshToken(): Boolean {
        val request: retrofit2.Response<LoginResponse> = apiService.refreshToken().execute()

        if (request.body() != null) {
            if (request.body()!!.success) {
                return true
            }
        }
        return false
    }

}