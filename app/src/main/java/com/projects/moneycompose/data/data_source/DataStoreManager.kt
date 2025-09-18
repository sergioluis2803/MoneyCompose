package com.projects.moneycompose.data.data_source

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {
    companion object{
        val INGRESO_KEY = stringPreferencesKey("ingreso")
    }

    val deposit: Flow<String> = context.dataStore.data.map{ preferences ->
        preferences[INGRESO_KEY] ?: ""
    }

    suspend fun saveIn(value: String){
        context.dataStore.edit { preferences ->
            preferences[INGRESO_KEY] = value
        }
    }
}