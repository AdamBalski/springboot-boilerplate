package pl.adambalski.springbootboilerplate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenDtoTest {
    @Test
    void testToJson() {
        JwtTokenDto jwtTokenDto = new JwtTokenDto("token");
        assertEquals("{\"token\": \"token\"}", jwtTokenDto.toJson());
    }

    @Test
    void testToJsonWithNull() {
        JwtTokenDto jwtTokenDto = new JwtTokenDto(null);
        assertEquals("{\"token\": \"null\"}", jwtTokenDto.toJson());
    }
}