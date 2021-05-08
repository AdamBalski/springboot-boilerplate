package pl.adambalski.springbootboilerplate.dto;

/**
 * Used to pass jwt token to an api consumer in AuthenticationController.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 */
@SuppressWarnings("ClassCanBeRecord")
public class JwtTokenDto {
    private final String token;

    public JwtTokenDto(String token) {
        this.token = token;
    }

    // This method is used when spring mvc tries to parse
    // the JwtTokenDto to JSON
    @SuppressWarnings("unused")
    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "{\"authorization\":\"" + token + "\"}";
    }
}