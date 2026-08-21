package com.arflix.tvdb

import org.json.JSONArray
import org.json.JSONObject

/**
 * Adapter/provider that maps TVDB responses into simple models the app can use.
 * Keep this small and focused so it can be integrated into the app's existing metadata layer.
 */
class TvdbMetadataProvider(private val client: TvdbClient) {

    fun fetchEpisodes(showId: String): List<TvdbEpisode> {
        val episodes = mutableListOf<TvdbEpisode>()
        var page = 1
        while (true) {
            val arr = client.fetchEpisodes(showId, page) ?: break
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                episodes.add(mapEpisode(obj))
            }
            // Basic pagination: if length < typical page size (100) then stop. Adjust as needed.
            if (arr.length() < 100) break
            page++
        }
        return episodes
    }

    private fun mapEpisode(obj: JSONObject): TvdbEpisode {
        // Defensive mapping: TVDB fields vary by API version and data completeness.
        val season = if (obj.has("airedSeason") && !obj.isNull("airedSeason")) obj.optInt("airedSeason") else null
        val episodeNumber = if (obj.has("airedEpisodeNumber") && !obj.isNull("airedEpisodeNumber")) obj.optInt("airedEpisodeNumber") else null
        val absoluteNumber = if (obj.has("absoluteNumber") && !obj.isNull("absoluteNumber")) obj.optInt("absoluteNumber") else null
        val airDate = when {
            obj.has("firstAired") && !obj.isNull("firstAired") -> obj.optString("firstAired")
            obj.has("airedDate") && !obj.isNull("airedDate") -> obj.optString("airedDate")
            else -> null
        }
        val dvdOrder = if (obj.has("dvdEpisodeNumber") && !obj.isNull("dvdEpisodeNumber")) {
            // sometimes DVD order is stored as string like "1" or "1.0"
            try {
                obj.optString("dvdEpisodeNumber").toInt()
            } catch (e: Exception) {
                null
            }
        } else null

        return TvdbEpisode(
            season = season,
            episodeNumber = episodeNumber,
            absoluteNumber = absoluteNumber,
            airDate = airDate,
            dvdOrder = dvdOrder
        )
    }
}
