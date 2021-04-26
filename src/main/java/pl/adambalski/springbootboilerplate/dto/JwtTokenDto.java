package pl.adambalski.springbootboilerplate.dto;

/**
 * Used to pass jwt token to an api consumer in AuthenticationController.<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @author Adam Balski
 */

public class JwtTokenDto {
    private String token;

    public JwtTokenDto(String token) {
        this.token = token;
    }

    public String getJwtToken() {
        return token;
    }

    @Override
    public String toString() {
        return "{\"authorization\":\"" + token + "\"}";
    }
}