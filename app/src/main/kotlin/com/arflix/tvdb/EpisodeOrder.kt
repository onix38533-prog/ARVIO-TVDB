package com.arflix.tvdb

/**
 * Episode ordering options exposed to users. Keep values names stable because they're persisted to preferences.
 */
enum class EpisodeOrder {
    COMBINED,
    ORIGINAL,
    ABSOLUTE,
    DVD,
    CUSTOM
}
