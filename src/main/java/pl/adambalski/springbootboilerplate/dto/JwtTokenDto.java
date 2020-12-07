package pl.adambalski.springbootboilerplate.dto;

public record JwtTokenDto(String token) {
    public String toJson() {
        StringBuilder result = new StringBuilder("{\"token\":\"");
        result.append(this.token);
        result.append("\"}");

        return result.toString();
    }
}
