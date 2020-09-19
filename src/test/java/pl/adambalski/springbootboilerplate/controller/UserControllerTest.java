package pl.adambalski.springbootboilerplate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import pl.adambalski.springbootboilerplate.exception.AuthenticationNotAccessibleException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserControllerTest {
    // Mocks
    @Mock
    UserController userController;

    // Data
    Authentication authentication;

    // returned from MockitoAnnotations.openMocks(this)
    AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);

        this.authentication = new UsernamePasswordAuthenticationToken(
                "username1",
                "password1"
        );
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }


    @Test
    void testGetUsername() {
        when(userController.getAuthentication())
                .thenReturn(authentication);
        when(userController.getUsername())
                .thenCallRealMethod();

        assertEquals("username1", userController.getUsername());
    }

    @Test
    void testGetUsernameWhenAuthenticationIsNull() {
        when(userController.getAuthentication())
                .thenReturn(null);
        when(userController.getUsername())
                .thenCallRealMethod();

        Executable executable = userController::getUsername;
        assertThrows(AuthenticationNotAccessibleException.class, executable);
    }
}