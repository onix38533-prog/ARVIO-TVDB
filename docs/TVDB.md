# TVDB integration and Episode Ordering

This branch adds a new, optional TVDB metadata provider and episode ordering settings.

Highlights
- Lightweight TVDB client (app/src/main/kotlin/com/arflix/tvdb/TvdbClient.kt) using HttpURLConnection and org.json.
- TvdbMetadataProvider maps TVDB responses into TvdbEpisode models and supports fetching (first pages) of episodes.
- Episode order enum and sorter (EpisodeOrder, EpisodeSorter) to apply ordering strategies (Combined, Original, Absolute, DVD, Custom).
- Preferences XML (app/src/main/res/xml/preferences_tvdb.xml) and helper TvdbPreferences to store the API key and ordering choice.

How to use
1. In Settings, add/import the preferences XML (preferences_tvdb.xml) into your Settings screen. Example:

   preferenceScreen.addPreferencesFromResource(R.xml.preferences_tvdb)

2. Enter your TVDB API key in the settings (it will be stored locally via SharedPreferences). Do NOT commit your key.

3. Select the Metadata source (TMDB or TVDB). When set to TVDB, create a TvdbClient using the stored key and fetch episodes via TvdbMetadataProvider.

Example (quick usage snippet)

```kotlin
val apiKey = TvdbPreferences.getApiKey(context) ?: ""
val client = TvdbClient(apiKey)
val provider = TvdbMetadataProvider(client)
val episodes = provider.fetchEpisodes("12345")
val order = TvdbPreferences.getEpisodeOrder(context)
val sorted = EpisodeSorter.sortEpisodes(episodes, order)
```

Notes and next steps
- The TVDB API has pagination and different auth flows (JWT). This implementation fetches pages until a page smaller than 100 items — adapt as needed.
- Integrate the provider into the app's existing metadata layer (MetadataProvider interface) so the UI toggles between TMDB and TVDB automatically.
- Add UI wiring to ensure episode lists use EpisodeSorter before rendering.
