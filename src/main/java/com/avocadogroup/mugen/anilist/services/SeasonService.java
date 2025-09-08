package com.avocadogroup.mugen.anilist.services;

import com.avocadogroup.mugen.anilist.AnimeSeasons;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SeasonService {
    // Function to get current season
    public AnimeSeasons getCurrentSeason() {
        // Get current month
        int month = LocalDate.now().getMonthValue();

        // If month is 1, 2, or 3, return WINTER
        if (month <= 3) {
            return AnimeSeasons.WINTER;
        } else  if (month <= 6) { // If month is 4, 5, or 6, return SPRING
            return AnimeSeasons.SPRING;
        } else if (month <= 9) { // If month is 7, 8, or 9, return SUMMER
            return AnimeSeasons.SUMMER;
        } else { // If month is 10, 11, or 12, return FALL
            return AnimeSeasons.FALL;
        }
    }

    // Function to get current year
    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}
