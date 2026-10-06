package com.arflix.tvdb

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Small helper to read/write TVDB-related preferences. Uses the default SharedPreferences.
 * Keys are public so they can be referenced from preference XML or other parts of the app.
 */
object TvdbPreferences {
    const val KEY_TVDB_API_KEY = "pref_tvdb_api_key"
    const val KEY_METADATA_SOURCE = "pref_metadata_source" // "tmdb" or "tvdb"
    const val KEY_EPISODE_ORDER = "pref_episode_order" // store enum name

    fun storeApiKey(context: Context, apiKey: String) {
        prefs(context).edit().putString(KEY_TVDB_API_KEY, apiKey).apply()
    }

    fun getApiKey(context: Context): String? = prefs(context).getString(KEY_TVDB_API_KEY, null)

    fun storeEpisodeOrder(context: Context, order: EpisodeOrder) {
        prefs(context).edit().putString(KEY_EPISODE_ORDER, order.name).apply()
    }

    fun getEpisodeOrder(context: Context): EpisodeOrder {
        val name = prefs(context).getString(KEY_EPISODE_ORDER, EpisodeOrder.COMBINED.name) ?: EpisodeOrder.COMBINED.name
        return try {
            EpisodeOrder.valueOf(name)
        } catch (e: Exception) {
            EpisodeOrder.COMBINED
        }
    }

    private fun prefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}
