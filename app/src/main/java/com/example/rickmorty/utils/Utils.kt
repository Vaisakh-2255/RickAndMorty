package com.example.rickmorty.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.rickmorty.App

class Utils {

    companion object {
        private const val NAME = "ricky&morty"

        private val preferences: SharedPreferences by lazy {
            App.instance()?.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                ?: throw IllegalStateException("App instance is not initialized")
        }

        private val editor: SharedPreferences.Editor by lazy {
            preferences.edit()
        }

        fun log(tag: String, value: String) {
            Log.d(tag, value)
        }

        fun getSharedPreferenceEdit(): SharedPreferences.Editor {
            return editor
        }

        fun getSharedPreference(): SharedPreferences {
            return preferences
        }

        fun hideKeyboard(context: Context, view: View) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isConnected == true
        }
    }
}
