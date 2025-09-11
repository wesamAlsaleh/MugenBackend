package com.avocadogroup.mugen.users.dtos;

import lombok.Data;

@Data
public class ChangeProfileRequest {
    private String username;
    private String email;
}
