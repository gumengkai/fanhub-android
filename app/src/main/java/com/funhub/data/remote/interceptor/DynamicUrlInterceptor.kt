package com.funhub.data.remote.interceptor

import com.funhub.data.repository.ServerAddressProvider
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class DynamicUrlInterceptor(
    private val serverAddressProvider: ServerAddressProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        
        // Get current server address
        val baseUrl = serverAddressProvider.getBaseUrl()
        val baseHttpUrl = baseUrl.toHttpUrlOrNull() ?: return chain.proceed(originalRequest)
        
        // Build new URL with current base URL host and port, keeping the path
        val newUrl = originalUrl.newBuilder()
            .scheme(baseHttpUrl.scheme)
            .host(baseHttpUrl.host)
            .port(baseHttpUrl.port)
            .build()
        
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        
        return chain.proceed(newRequest)
    }
}
