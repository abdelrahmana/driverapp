package com.tt.driver.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val loggedInKey = booleanPreferencesKey("user_logged_in")

    private val tokenKey = stringPreferencesKey("user_token")

    private val fcmTokenKey = stringPreferencesKey("fcm_token")

    fun isUserLoggedIn(): Flow<Boolean> {
        return context.dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[loggedInKey] ?: false
            }
    }

    fun changeAuthStatus(coroutineScope: CoroutineScope, login: Boolean) {
        coroutineScope.launch(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[loggedInKey] = login
            }
        }
    }

    fun getToken(): Flow<String> {
        return context.dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[tokenKey] ?: ""
            }
    }

    fun setUserToken(coroutineScope: CoroutineScope, token: String) {
        coroutineScope.launch(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[tokenKey] = token
            }
        }
    }

    fun getFCMToken(): Flow<String> {
        return context.dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[fcmTokenKey] ?: ""
            }
    }

    fun setFCMToken(coroutineScope: CoroutineScope, token: String) {
        coroutineScope.launch(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[fcmTokenKey] = token
            }
        }
    }

}