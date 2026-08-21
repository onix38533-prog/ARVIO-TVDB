package com.arflix.tvdb

/**
 * Simple model for episodes produced by the TVDB provider.
 * This is intentionally local to the tvdb package to avoid colliding with the app's existing Episode model.
 */
data class TvdbEpisode(
    val season: Int? = null,
    val episodeNumber: Int? = null,
    val absoluteNumber: Int? = null,
    val airDate: String? = null, // ISO date if available (yyyy-MM-dd)
    val dvdOrder: Int? = null
)
