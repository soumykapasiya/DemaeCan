package com.kapasiya.demaecan.response;

import com.kapasiya.demaecan.model.USER_ROLE;
import lombok.Data;

@Data
public class AuthReponse
{
    private String jwt;

    private String message;

    private USER_ROLE role;
}
