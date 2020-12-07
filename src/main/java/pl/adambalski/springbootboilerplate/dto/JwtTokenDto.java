package pl.adambalski.springbootboilerplate.dto;

/**
 * Used to pass jwt token to an api consumer in AuthenticationController.<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController;
 * @author Adam Balski
 */

public record JwtTokenDto(String token) {
    public String toJson() {
        StringBuilder result = new StringBuilder("{\"token\": \"");
        result.append(this.token);
        result.append("\"}");

        return result.toString();
    }
}
