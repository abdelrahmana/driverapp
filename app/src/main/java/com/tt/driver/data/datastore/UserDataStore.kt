package com.tt.driver.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.tt.driver.data.models.entities.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")


    private val userInKey = stringPreferencesKey("user_data")

    fun getUser(): Flow<User?> {
        return context.dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                try {
                    Gson().fromJson(preferences[userInKey], User::class.java)
                } catch (e: Exception) {
                    null
                }
            }
    }

    fun setUserData(coroutineScope: CoroutineScope, user: User) {
        coroutineScope.launch(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[userInKey] = Gson().toJson(user, User::class.java)
            }
        }
    }

    fun updateOnlineStatus(coroutineScope: CoroutineScope, isOnline: Boolean) {
        coroutineScope.launch(Dispatchers.IO) {
            getUser().first()?.apply { updateStatus(isOnline) }?.let {
                setUserData(coroutineScope, it)
            }
        }
    }

}