package pl.adambalski.springbootboilerplate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JwtTokenDtoTest {
    @Test
    void testToString() {
        JwtTokenDto jwtTokenDto = new JwtTokenDto("jwt_token");
        String expectedResult = "{\"authorization\":\"jwt_token\"}";

        assertEquals(expectedResult, jwtTokenDto.toString());
    }

    @Test
    void testToStringWhenTokenIsNull() {
        JwtTokenDto jwtTokenDto = new JwtTokenDto(null);
        String expectedResult = "{\"authorization\":\"null\"}";

        assertEquals(expectedResult, jwtTokenDto.toString());
    }
}