package pl.adambalski.springbootboilerplate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RestUserControllerTest {
    // Components
    RestUserController userController;

    // Data
    Authentication authentication;

    @BeforeEach
    void init() {
        userController = new RestUserController();
        authentication = new UsernamePasswordAuthenticationToken("username3", "password3");
    }

    @Test
    void testGetAuthenticationWhenAuthenticationIsSet() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertEquals(authentication, userController.getAuthentication());
    }

    @Test
    void testGetAuthenticationWhenAuthenticationIsNull() {
        SecurityContextHolder.clearContext();

        assertNull(userController.getAuthentication());
    }
}