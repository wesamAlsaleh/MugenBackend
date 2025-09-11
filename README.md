# 🎌 Mugen — Anime Tracking Backend (Spring Boot)

## Features
- User authentication (JWT).
- Track anime status (Watching, Paused, Planning, Dropper, Completed).
- Fetch studio details.
- View top anime of the current season.
- Explore by genre, season, title.

## Tech Stack
- Java 24 + Spring Boot 3 + Maven
- Spring Security + JWT
- MySQL
- GraphQL (Anilist Public API)
- Swagger UI for API docs

## API Reference

#### Get This Season Animes

```http
  GET /anime/this-season-animes
```
| Parameter | Type  | Description                                                |
|:----------|:------|:-----------------------------------------------------------|
| `page`    | `int` | **Not Required**. number of page, By default 1.            |
| `perPage` | `int` | **Not Required**. number of items per page, By default 15. |

#### Get This Season Top Animes
```http
  GET /anime/top-this-season-animes
```
| Parameter | Type | Description |
|:----------|:-----|:------------|
| -         | -    | -           |



#### Search Animes By Title
```http
  GET /anime/search-animes
```

| Parameter     | Type         | Description                                                                        |
|:--------------|:-------------|:-----------------------------------------------------------------------------------|
| `page`        | `int`        | **Not Required**. number of page, By default 1.                                    |
| `perPage`     | `int`        | **Not Required**. number of items per page, By default 10.                         |                               |
| `type`        | `MediaTypes` | ** Required**. media type, can be `ANIME` or `MANGA` as well, By default `ANIME`.  |
| `searchQuery` | `String`     | **Required**. the search string to search for (can be a title or part of a title). |

#### Explore Animes with Filters
```http
  GET /anime/explore-animes
```

| Parameter    | Type           | Description                                                                                                     |
|:-------------|:---------------|:----------------------------------------------------------------------------------------------------------------|
| `page`       | `int`          | **Not Required**. number of page, By default 1.                                                                 |
| `perPage`    | `int`          | **Not Required**. number of items per page, By default 10.                                                      |                               |
| `type`       | `MediaTypes`   | **Required**. media type, can be `ANIME` or `MANGA` as well, By default `ANIME`.                                |
| `season`     | `AnimeSeasons` | **Not Required**. media release season, cab ve `WINTER`, `SPRING`, `SUMMER`, `FALL`, By default current season. |
| `seasonYear` | `seasonYear`   | **Not Required**. media release year, By default current year.                                                  |
| `genres`     | `List<String>` | **Not Required**. list of genres to filter by.                                                                  |

#### Get All Genres
```http
  GET /anime/genres
```
| Parameter | Type | Description |
|:----------|:-----|:------------|
| -         | -    | -           |


#### Get Studio Details
```http
  GET /anime/studio
```
| Parameter | Type  | Description                                                |
|:----------|:------|:-----------------------------------------------------------|
| `page`    | `int` | **Not Required**. number of page, By default 1.            |
| `perPage` | `int` | **Not Required**. number of items per page, By default 10. |
| `id`      | `int` | **Required**. studio id.                                   |

## API Documentation

- To see the full API documentation, access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

*(Start the app first — docs auto-generate from code)*










