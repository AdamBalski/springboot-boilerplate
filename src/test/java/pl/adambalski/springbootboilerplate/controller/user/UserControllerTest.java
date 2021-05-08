package pl.adambalski.springbootboilerplate.controller.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.service.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserControllerTest {
    AutoCloseable autoCloseable;

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    User user;
    String login;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        login = "username";
        user = new User(
                UUID.randomUUID(),
                login,
                "Full Name",
                "e@mail.jpg",
                "",
                Role.USER
        );

        setContext();

    }

    private void setContext() {
        SecurityContextHolder.clearContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(login, "");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void testGetOwnData() {
        Mockito.when(userService.getUserByLogin(login)).thenReturn(user);

        assertEquals(this.user, userController.getOwnData());
        verify(userService).getUserByLogin(login);
    }

    @Test
    void testDeleteLoggedUser() {
        userController.deleteLoggedUser();
        verify(userService).deleteUserByLogin(login);
    }

    @Test
    void testSignUpWhenEverythingIsOk() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "!",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        Executable executable = () -> userController.signUp(signUpUserDto);
        assertDoesNotThrow(executable);

        verify(userService).addSignUpUserDto(signUpUserDto);
    }

    @Test
    void testSignUpWhenLoginIsInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "!",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "LOGIN_IS_INCORRECT");
        Mockito.doThrow(exception).when(userService).addSignUpUserDto(signUpUserDto);

        Executable executable = () -> userController.signUp(signUpUserDto);
        assertThrows(ResponseStatusException.class, executable);

        verify(userService).addSignUpUserDto(signUpUserDto);
    }
}