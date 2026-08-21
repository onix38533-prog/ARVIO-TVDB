package com.arflix.tvdb

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

/**
 * Lightweight TVDB client using HttpURLConnection and org.json to avoid adding new dependencies.
 *
 * Notes:
 * - Do NOT store API keys in source control. Pass the API key from settings (TvdbPreferences) or BuildConfig.
 * - The official TVDB API may require a JWT authentication flow; this client attempts a simple Authorization header
 *   ("Bearer {apiKey}") which works for some setups. You may need to adjust auth depending on your TVDB account.
 */
class TvdbClient(private val apiKey: String) {
    private val base = "https://api.thetvdb.com"

    private fun get(path: String, addAuth: Boolean = true): JSONObject? {
        val url = URL(base + path)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 15000
            readTimeout = 15000
            if (addAuth && apiKey.isNotBlank()) {
                setRequestProperty("Authorization", "Bearer $apiKey")
            }
            setRequestProperty("Accept", "application/json")
        }

        return try {
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val reader = BufferedReader(InputStreamReader(stream))
            val sb = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            JSONObject(sb.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            conn.disconnect()
        }
    }

    /**
     * Fetch full show details (series) as raw JSONObject. showId is the TVDB series id.
     */
    fun fetchShow(showId: String): JSONObject? {
        return get("/series/$showId")
    }

    /**
     * Fetch episodes for a series. This returns the raw data array if successful, otherwise null.
     * NOTE: TVDB paginates results. This helper fetches first page only. You can extend it to follow pagination links.
     */
    fun fetchEpisodes(showId: String, page: Int = 1): JSONArray? {
        val resp = get("/series/$showId/episodes?page=$page") ?: return null
        return try {
            // Some TVDB responses put the episodes under "data"
            when {
                resp.has("data") -> resp.getJSONArray("data")
                resp.has("results") -> resp.getJSONArray("results")
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
