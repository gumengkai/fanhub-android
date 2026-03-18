package com.funhub.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class ServerAddressProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ServerAddressProvider {

    companion object {
        private val SERVER_ADDRESS = stringPreferencesKey("server_address")
        private const val DEFAULT_SERVER = "http://192.168.1.100:5000"
    }

    override fun getBaseUrl(): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[SERVER_ADDRESS] ?: DEFAULT_SERVER
            }.first()
        }
    }
}
