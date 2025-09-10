package com.avocadogroup.mugen.anilist.services;

import com.avocadogroup.mugen.anilist.dtos.*;
import com.avocadogroup.mugen.anilist.enums.MediaSortBy;
import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Note: Map is more similar to an object in JSON

@Service
@AllArgsConstructor
public class AnilistService {
    private final SeasonService seasonService;
    private final GraphQlService graphQlService;

    // Function to fetch this season's anime list using graphql
    public ThisSeasonAnimesResponse fetchThisSeasonAnimes(ThisSeasonAnimesRequest request)  {
        // Prepare the GraphQL query
        String query = """
            query Query($page: Int, $perPage: Int, $season: MediaSeason, $seasonYear: Int) {
                       Page(page: $page, perPage: $perPage) {
                         media(season: $season, seasonYear: $seasonYear) {
                           id
                           title {
                             english
                             native
                             romaji
                             userPreferred
                           }
                           coverImage {
                             color
                             extraLarge
                             large
                             medium
                           }
                           averageScore
                           meanScore
                           type
                           status
                           episodes
                           genres
                           nextAiringEpisode {
                             airingAt
                             episode
                           }
                         }
                       }
                     }
        """;

        // Prepare the variables for the query in a map {page: 1, perPage: 10, season: "SPRING", seasonYear: 2024}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", request.getPage());
        variables.put("perPage", request.getPerPage());
        variables.put("season", seasonService.getCurrentSeason());
        variables.put("seasonYear", seasonService.getCurrentYear());

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        List<AnimeDto> mediaList = (List<AnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables); // Cast to List<AnimeDto> to avoid type mismatch

        // Return the list of animes wrapped in a ThisSeasonAnimesResponse object
        return new ThisSeasonAnimesResponse(mediaList);
    }

    // Function to fetch top this season's anime list based on average score
    public ThisSeasonTopAnimesResponse fetchThisSeasonTopAnimes() {
        // Prepare the GraphQL query
        String query = """
            query Query($perPage: Int, $season: MediaSeason, $seasonYear: Int, $sort: [MediaSort]) {
                Page(perPage: $perPage) {
                    media(season: $season, seasonYear: $seasonYear, sort: $sort) {
                        id
                        title {
                            english
                            native
                            romaji
                            userPreferred
                        }
                        averageScore
                        meanScore
                        status
                        nextAiringEpisode {
                            airingAt
                            episode
                        }
                    }
                }
            }
        """;

        // Prepare the variables for the query in a map {page: 1, perPage: 10, season: "SPRING", seasonYear: 2024}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", 15); // Hardcoded to get more results for top animes
        variables.put("season", seasonService.getCurrentSeason());
        variables.put("seasonYear", seasonService.getCurrentYear());
        variables.put("sort", MediaSortBy.SCORE_DESC);

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        List<TopAnimeDto> mediaList = (List<TopAnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables); // Cast to List<TopAnimeDto> to avoid type mismatch

        // Return the list of animes
        return new ThisSeasonTopAnimesResponse(mediaList);
    }

    // Function to search animes by title with pagination
    public SearchAnimesResponse searchAnimes(SearchAnimesRequest request) {
        // Prepare the GraphQL query
        String query = """
            query Query($sort: [MediaSort], $type: MediaType, $search: String, $perPage: Int, $page: Int) {
                Page(perPage: $perPage, page: $page) {
                    media(sort: $sort, type: $type, search: $search) {
                        id
                        title {
                            english
                            native
                            romaji
                            userPreferred
                        }
                        coverImage {
                            color
                            extraLarge
                            large
                            medium
                        }
                        averageScore
                        meanScore
                        status
                        episodes
                        nextAiringEpisode {
                            airingAt
                            episode
                        }
                    }
                }
            }
        """;

        // Prepare the variables for the query in a map {page: 1, perPage: 10, type: "ANIME", search: "Naruto"}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", request.getPage());
        variables.put("perPage", request.getPerPage());
        variables.put("type", request.getType());
        variables.put("search", request.getSearchQuery());
        variables.put("sort", MediaSortBy.TRENDING_DESC); // Sort by trending by default

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        var mediaList = (List<SearchResultAnimesDto>) graphQlService.postGraphQLRequestToAnilist(query, variables);

        // Return the list of animes wrapped in a SearchAnimesResponse object
        return new SearchAnimesResponse(mediaList);
    }

    // Function to fetch specific animes by their IDs
    public AnimeListResponse fetchAnimesByIds(AnimeListRequest request) {
        // Prepare the GraphQL query
        String query = """
                query Query($idIn: [Int], $perPage: Int, $page: Int) {
                     Page(perPage: $perPage, page: $page) {
                       media(id_in: $idIn) {
                         id
                         title {
                           english
                           native
                           romaji
                           userPreferred
                         }
                         coverImage {
                           color
                           extraLarge
                           large
                           medium
                         }
                         averageScore
                         meanScore
                         type
                         status
                         episodes
                         genres
                         nextAiringEpisode {
                           airingAt
                           episode
                         }
                       }
                     }
                   }
                """;

        // Prepare the variables for the query in a map {idIn: [185407, 178788, 181444, 182309, 185660, 154768, 175914, 171046]}
        Map<String, Object> variables = new HashMap<>();
        variables.put("page", request.getPage());
        variables.put("perPage", request.getPerPage());
        variables.put("idIn", request.getAnimeIds());

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        var mediaList = (List<AnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables);

        // Return the list of animes
        return new AnimeListResponse(mediaList);
    }

    // Function to explore animes by genre with pagination
    public ExploreAnimesByGenreResponse exploreAnimes(ExploreAnimesByGenreRequest request) {
        // Prepare the GraphQL query
        String query = """
                query Query($perPage: Int, $page: Int, $season: MediaSeason, $seasonYear: Int, $genreIn: [String], $sort: [MediaSort], $type: MediaType) {
                    Page(perPage: $perPage, page: $page) {
                      media(season: $season, seasonYear: $seasonYear, genre_in: $genreIn, sort: $sort, type: $type) {
                        id
                        title {
                          english
                          native
                          romaji
                          userPreferred
                        }
                        coverImage {
                          color
                          extraLarge
                          large
                          medium
                        }
                        averageScore
                        meanScore
                        type
                        status
                        episodes
                        genres
                        nextAiringEpisode {
                          airingAt
                          episode
                        }
                      }
                      }
                    }
                """;

        // Prepare the variables for the query in a map
        Map<String, Object> variables = new HashMap<>();
        variables.put("perPage", request.getPerPage());
        variables.put("page", request.getPage());
        variables.put("season", null);
        variables.put("seasonYear", request.getSeasonYear());
        variables.put("genreIn", request.getGenres() != null && !request.getGenres().isEmpty() ? request.getGenres() : null); // Must be in this format ["ACTION", "ADVENTURE", "FANTASY"] or null
        variables.put("sort", MediaSortBy.TRENDING_DESC); // Sort by trending by default
        variables.put("type", request.getType());

        // If year is provided but season is not, put default to current season
        if (request.getSeasonYear() != null && request.getSeason() == null) {
            // If season is not provided, default to current season
            variables.put("season", seasonService.getCurrentSeason()); // If season is provided but year is not, the API by default fetch random animes from that season but from any year
        } else {
            variables.put("season", request.getSeason());
        }

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        var mediaList = (List<AnimeDto>) graphQlService.postGraphQLRequestToAnilist(query, variables);

        // Return the list of animes
        return new ExploreAnimesByGenreResponse(mediaList);
    }

    // Function to fetch the list of genres (Not available from Anilist)
    public GenresResponse fetchGenres(UserPreferredLanguage language) {
        // Since Anilist does not provide an endpoint to fetch genres, we will return a hardcoded list of genres
        List<String> EnGenres = List.of(
                "Action", "Adventure", "Comedy", "Drama", "Ecchi", "Fantasy", "Horror",
                "Mahou Shoujo", "Mecha", "Music", "Mystery", "Psychological", "Romance",
                "Sci-Fi", "Slice of Life", "Sports", "Supernatural", "Thriller"
        );

        List<String> ArGenres = List.of(
                "أكشن", "مغامرة", "كوميديا", "دراما", "إيتشي", "فانتازيا", "رعب",
                "ماهو شوجو", "ميكا", "موسيقى", "غموض", "نفسية", "رومانسية",
                "خيال علمي", "شريحة من الحياة", "رياضة", "خارق للطبيعة", "إثارة"
        );

        // Return the list of genres wrapped in a GenresResponse object based on user preferred language
        var genres = language == UserPreferredLanguage.AR ? ArGenres : EnGenres;

        return new GenresResponse(genres);
    }

    // Function to fetch anime details by anime ID
    public AnimeDetailsResponse fetchAnimeDetailsById(Integer animeId) {
        // Prepare the GraphQL query
        String query = """
                query Query($mediaId: Int) {
                    Page {
                        media(id: $mediaId) {
                            id
                            idMal
                            title {
                                romaji
                                english
                                native
                                userPreferred
                            }
                            type
                            format
                            status
                            description
                            startDate {
                                day
                                month
                                year
                            }
                            endDate {
                                day
                                month
                                year
                            }
                            season
                            seasonYear
                            episodes
                            duration
                            countryOfOrigin
                            source
                            hashtag
                            trailer {
                                id
                                site
                                thumbnail
                            }
                            coverImage {
                                color
                                extraLarge
                                large
                                medium
                            }
                            bannerImage
                            genres
                            averageScore
                            meanScore
                            popularity
                            trending
                            studios {
                                edges {
                                    id
                                    isMain
                                    node {
                                        id
                                        name
                                        siteUrl
                                        isAnimationStudio
                                    }
                                }
                            }
                            characters {
                                edges {
                                    role
                                        node {
                                            id
                                            name {
                                                full
                                                userPreferred
                                            }
                                            age
                                            gender
                                            description
                                            image {
                                                large
                                                medium
                                            }
                                            siteUrl
                                        }
                                        voiceActors {
                                            id
                                            name {
                                                full
                                                userPreferred
                                            }
                                            image {
                                                large
                                                medium
                                            }
                                            siteUrl
                                        }
                                    }
                                }
                            isAdult
                            nextAiringEpisode {
                                airingAt
                                episode
                                timeUntilAiring
                            }
                            siteUrl
                            relations {
                                edges {
                                    relationType
                                    node {
                                        id
                                        title {
                                            english
                                            native
                                            romaji
                                            userPreferred
                                        }
                                        coverImage {
                                            large
                                            medium
                                        }
                                        type
                                        format
                                        status
                                        episodes
                                        siteUrl
                                    }
                                }
                            }
                            recommendations {
                                edges {
                                    node {
                                        rating
                                        mediaRecommendation {
                                            id
                                            title {
                                                english
                                                native
                                                romaji
                                                userPreferred
                                            }
                                            coverImage {
                                                large
                                                medium
                                            }
                                            type
                                            format
                                            status
                                            episodes
                                            siteUrl
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                """;

        // Prepare the variables for the query in a map
        Map<String, Object> variables = new HashMap<>();
        variables.put("mediaId", animeId);

        // Try to make the POST request to AniList GraphQL endpoint with the query and variables
        var anime = (List<AnimeDetailsDto>) graphQlService.postGraphQLRequestToAnilist(query, variables);

        // Return the anime details wrapped in a AnimeDetailsResponse object
        return new AnimeDetailsResponse(anime);
    }

}


// TODO: infinite scrolling, by making the frontend call this endpoint with incrementing page numbers

// Sample anime Ids [185407, 178788, 181444, 182309, 185660, 154768, 175914, 171046]