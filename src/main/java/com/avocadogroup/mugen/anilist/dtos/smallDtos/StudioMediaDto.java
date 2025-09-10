package com.avocadogroup.mugen.anilist.dtos.smallDtos;

import lombok.Data;

import java.util.List;

@Data
public class StudioMediaDto {
    private List<StudioEdgeDto> edges;
}


// List<StudioEdgeDto> edges is because the response from Anilist GraphQL API for studios media is structured with an "edges" array.
// The array is in a different format than a simple list of media nodes.
// Each item in the "edges" array contains a "node" object that holds the actual media details, sample:

/**
 *       "media": {
 *         "edges": [ // As you can see, edges is an array of objects [ { and each object contain a Node of type "StudioEdgeDto" }, {"node" : {}} ]
 *           {
 *             "node": {
 *               "id": 102,
 *               "title": {
 *                 "english": "Love You Baby",
 *                 "native": "愛してるぜ ベイベ★★",
 *                 "romaji": "Aishiteruze Baby★★",
 *                 "userPreferred": "Aishiteruze Baby★★"
 *               },
 *               "coverImage": {
 *                 "color": "#f1ae43",
 *                 "extraLarge": "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx102-nacpPTHmjvXJ.png",
 *                 "large": "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx102-nacpPTHmjvXJ.png",
 *                 "medium": "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx102-nacpPTHmjvXJ.png"
 *               },
 *               "season": "SPRING",
 *               "seasonYear": 2004,
 *               "averageScore": 71,
 *               "meanScore": 71,
 *               "type": "ANIME",
 *               "status": "FINISHED",
 *               "episodes": 26,
 *               "genres": [
 *                 "Comedy",
 *                 "Drama",
 *                 "Romance"
 *               ],
 *               "nextAiringEpisode": null,
 *               "siteUrl": "https://anilist.co/anime/102"
 *             }
 *           }
 *        ] // End of edges array
 *
 */