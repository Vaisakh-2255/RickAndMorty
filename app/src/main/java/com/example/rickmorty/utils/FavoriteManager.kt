package com.example.rickmorty.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.rickmorty.model.Character
import com.google.gson.Gson
import java.lang.reflect.Type

class FavoriteManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    fun addFavorite(character: Character) {
        val favorites = favorites
        favorites.add(character.id ?: return) // Safeguard in case id is null
        saveFavorites(favorites)
    }

    fun removeFavorite(character: Character) {
        val favorites = favorites
        favorites.remove(character.id ?: return) // Safeguard in case id is null
        saveFavorites(favorites)
    }

    fun isFavorite(characterId: String): Boolean {
        val favorites: Set<String> = favorites
        return favorites.contains(characterId)
    }

    private val favorites: MutableSet<String>
        get() {
            val json = sharedPreferences.getString(FAVORITES_KEY, "[]")
            val type: Type = object : com.google.gson.reflect.TypeToken<Set<String>>() {}.type
            return gson.fromJson(json, type) ?: mutableSetOf()
        }

    private fun saveFavorites(favorites: Set<String>) {
        val editor = sharedPreferences.edit()
        val json: String = gson.toJson(favorites)
        editor.putString(FAVORITES_KEY, json)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "favorite_prefs"
        private const val FAVORITES_KEY = "favorites"
    }
}
