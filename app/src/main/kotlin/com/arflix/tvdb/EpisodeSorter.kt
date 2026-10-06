package com.arflix.tvdb

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Utility to sort lists of TvdbEpisode according to user's selected EpisodeOrder.
 * The function is defensive: it handles nulls and best-effort parses dates in ISO format.
 */
object EpisodeSorter {

    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private fun parseDate(dateStr: String?): LocalDate? {
        if (dateStr == null) return null
        return try {
            LocalDate.parse(dateStr, isoFormatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun sortEpisodes(episodes: List<TvdbEpisode>, order: EpisodeOrder): List<TvdbEpisode> {
        return when (order) {
            EpisodeOrder.COMBINED -> episodes.sortedWith(compareBy({ it.season ?: Int.MAX_VALUE }, { it.episodeNumber ?: Int.MAX_VALUE }))
            EpisodeOrder.ORIGINAL -> episodes.sortedBy { parseDate(it.airDate) ?: LocalDate.MAX }
            EpisodeOrder.ABSOLUTE -> episodes.sortedBy { it.absoluteNumber ?: Int.MAX_VALUE }
            EpisodeOrder.DVD -> episodes.sortedBy { it.dvdOrder ?: Int.MAX_VALUE }
            EpisodeOrder.CUSTOM -> episodes // keep as-is; app can provide custom ordering UI later
        }
    }
}
