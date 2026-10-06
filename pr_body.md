# Pull Request: Add optional TVDB provider and episode ordering settings

This PR introduces optional TVDB metadata support and a new Episode Ordering setting.

Summary of changes:
- Added a lightweight TVDB HTTP client and a metadata provider that maps TVDB responses into app-friendly episode models.
- Added EpisodeOrder enum and EpisodeSorter utility to order episode lists according to the user's preference.
- Added preferences XML and preference strings for entering the TVDB API key, selecting the metadata source (TMDB/TVDB), and choosing the episode ordering.
- Documentation added at docs/TVDB.md with usage notes and integration steps.

Security note:
- No API keys are committed. Please add your TVDB API key in Settings (TVDB API Key) or use BuildConfig/secrets for local testing.

Testing steps:
1. Switch the metadata source to TVDB in Settings and enter a valid TVDB API key.
2. Open a show's episode list. The app codebase needs to use TvdbMetadataProvider when the metadata source is TVDB — see docs/TVDB.md example.
3. Change Episode ordering in Settings and verify the episode list order updates accordingly.

Future improvements:
- Integrate TvdbMetadataProvider into the app's central metadata provider factory so switching sources is automatic.
- Improve TVDB auth flow (JWT, token refresh) if necessary.
- Add more robust pagination handling and caching.

