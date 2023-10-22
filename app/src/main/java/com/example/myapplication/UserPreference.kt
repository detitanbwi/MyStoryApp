package com.example.myapplication

import android.content.Context
import com.example.myapplication.data.UserModel

internal class UserPreference(context : Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"
        private const val USER_ID = "user_id"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.putString(USER_ID, value.userId)
        editor.apply()
    }
    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.userId = preferences.getString(USER_ID, "")
        model.token = preferences.getString(TOKEN, "")
        return model
    }
}