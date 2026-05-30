package com.example.film_catalog_android.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userSessionDataStore by preferencesDataStore(name = "user_session")

object UserSessionStorage {

    private lateinit var appContext: Context

    val token: Flow<String?>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }

    val userId: Flow<Long?>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }

    val isAuthorized: Flow<Boolean>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[IS_AUTHORIZED_KEY] ?: false
        }

    val isAdmin: Flow<Boolean>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[ROLE_KEY] == ROLE_ADMIN
        }

    val firstName: Flow<String>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[FIRST_NAME_KEY] ?: ""
        }

    val lastName: Flow<String>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[LAST_NAME_KEY] ?: ""
        }

    val role: Flow<String>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[ROLE_KEY] ?: ""
        }

    val email: Flow<String>
        get() = appContext.userSessionDataStore.data.map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    suspend fun saveSession(
        token: String,
        userId: Long,
        firstName: String,
        lastName: String,
        email: String,
        role: String
    ) {
        appContext.userSessionDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[FIRST_NAME_KEY] = firstName
            preferences[LAST_NAME_KEY] = lastName
            preferences[EMAIL_KEY] = email
            preferences[ROLE_KEY] = role
            preferences[IS_AUTHORIZED_KEY] = true
        }
    }

    suspend fun clearSession() {
        appContext.userSessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val USER_ID_KEY = longPreferencesKey("user_id")
    private val FIRST_NAME_KEY = stringPreferencesKey("first_name")
    private val LAST_NAME_KEY = stringPreferencesKey("last_name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val ROLE_KEY = stringPreferencesKey("role")
    private val IS_AUTHORIZED_KEY = booleanPreferencesKey("is_authorized")

    private const val ROLE_ADMIN = "ADMIN"
}
