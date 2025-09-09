package com.avocadogroup.mugen.anilist.dtos;

import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import lombok.Data;

@Data
public class GenresRequest {
    private UserPreferredLanguage lang = UserPreferredLanguage.EN; // Default to English
}
