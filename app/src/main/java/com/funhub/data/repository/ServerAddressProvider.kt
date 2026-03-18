package com.funhub.data.repository

interface ServerAddressProvider {
    fun getBaseUrl(): String
    suspend fun saveBaseUrl(url: String)
}
