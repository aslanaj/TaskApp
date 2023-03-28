package com.example.taskapp.data.local

import android.content.Context


class ProfilePref(context: Context) {
    private val profilePref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getNameText(): String? {
        return profilePref.getString(PROFILE_NAME_TEXT, null)
    }

    fun saveProfileNameText(text: String) {

        profilePref.edit().putString(PROFILE_NAME_TEXT, text).apply()
    }

    fun saveImageUri(image: String) {
        profilePref.edit().putString(IMAGE_URI, image).apply()
    }

    fun getImageUri(): String {
     return profilePref.getString(IMAGE_URI, "").toString()

    }

    companion object {
        const val PREF_NAME = "task.name.53"
        const val PROFILE_NAME_TEXT = "name"
        const val IMAGE_URI = "imageUri"
    }
}