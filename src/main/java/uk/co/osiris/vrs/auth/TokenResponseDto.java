package uk.co.osiris.vrs.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String token;
    private String tokenType = "Bearer";

    public TokenResponseDto(String token) {
        this.token = token;
    }
}
