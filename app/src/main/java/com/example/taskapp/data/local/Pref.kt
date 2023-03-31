package com.example.taskapp.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Pref(context: Context) {

    private val pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    fun isUserSeen(): Boolean {
        return pref.getBoolean(ONBOARD_KEY, false)
    }

    fun saveSeen() {
        pref.edit().putBoolean(ONBOARD_KEY, true).apply()
    }

    fun saveAnyText(text: String) {
        pref.edit().putString(ANY_TEXT, text).apply()
    }

    fun getAnyText() = pref.getString(ANY_TEXT, "")
    fun getNameText(): String? {
        return pref.getString(Pref.PROFILE_NAME_TEXT, null)
    }

    fun saveProfileNameText(text: String) {

        pref.edit().putString(Pref.PROFILE_NAME_TEXT, text).apply()
    }

    fun saveImageUri(image: String) {
        pref.edit().putString(Pref.IMAGE_URI, image).apply()
    }

    fun getImageUri(): String {
        return pref.getString(Pref.IMAGE_URI, "").toString()

    }

    companion object {
        const val PREF_NAME = "task.name.53"
        const val ONBOARD_KEY = "onBoardKey"
        const val ANY_TEXT = "task.name.53"
        const val PROFILE_NAME_TEXT = "name"
        const val IMAGE_URI = "imageUri"
    }
}