package com.example.librarymanagementsystem

import android.content.Context
import android.content.SharedPreferences

class SharedPref private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun storeToken(token: String): Boolean {
        val editor = sharedPreferences.edit()
        editor.putString(KEY, token)
        return editor.commit()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY, null)
    }

    companion object {
        private const val NAME = "FCM"
        private const val KEY = "Key"
        private var mInstance: SharedPref? = null

        @JvmStatic
        fun getInstance(context: Context): SharedPref {
            if (mInstance == null) {
                mInstance = SharedPref(context)
            }
            return mInstance as SharedPref
        }
    }
}
