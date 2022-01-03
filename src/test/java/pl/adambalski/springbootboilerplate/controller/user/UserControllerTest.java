package pl.adambalski.springbootboilerplate.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.EmailIsTakenException;
import pl.adambalski.springbootboilerplate.exception.LoginIsTakenException;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;
import pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;
import pl.adambalski.springbootboilerplate.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserController userController;

    @MockBean
    UserService userService;

    // ApplicationContext wants repositories (services -> repositories),
    // to inject to services,
    // but springboot can't instantiate repositories with @WebMvcTest, so we
    // have to create mocked beans.
    @MockBean
    AdminRepository adminRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    SignUpUserDto mockUserDto;
    User mockUser;

    AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        mockUserDto = new SignUpUserDto(
                "mockusername",
                "Mock User Name",
                "mock@username.com",
                "password",
                "password");
        mockUser = User.valueOf(mockUserDto, new PasswordEncoderFactory().passwordEncoderBean());
    }

    @AfterEach
    void destroy() throws Exception {
        autoCloseable.close();
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testGetOwnDataAsAdmin() throws Exception {
        String login = mockUser.getLogin();

        when(userService.getUserByLogin(login))
                .thenReturn(mockUser);

        mockUser.setPassword(null);
        mockUser.setUuid(null);
        String mockUserJson = new ObjectMapper().writeValueAsString(mockUser);

        mockMvc.perform(get("/api/user/get-data"))
                .andExpect(content().json(mockUserJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testGetOwnDataAsAdminWhenThereIsNoSuchUser() throws Exception {
        String login = mockUser.getLogin();

        when(userService.getUserByLogin(login))
                .thenThrow(new NoSuchUserException());


        mockMvc.perform(get("/api/user/get-data"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testGetOwnDataAsUserWhenThereIsNoSuchUser() throws Exception {
        String login = mockUser.getLogin();

        when(userService.getUserByLogin(login))
                .thenThrow(new NoSuchUserException());


        mockMvc.perform(get("/api/user/get-data"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testGetOwnDataAsUser() throws Exception {
        String login = mockUser.getLogin();

        when(userService.getUserByLogin(login))
                .thenReturn(mockUser);

        mockUser.setPassword(null);
        mockUser.setUuid(null);
        String mockUserJson = new ObjectMapper().writeValueAsString(mockUser);

        mockMvc.perform(get("/api/user/get-data"))
                .andExpect(content().json(mockUserJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetOwnDataAsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/user/get-data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testDeleteLoggedUserAsAdmin() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/user/delete-logged-user")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userService).deleteUserByLogin(login);
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testDeleteLoggedUserAsUser() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/user/delete-logged-user")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userService).deleteUserByLogin(login);
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testDeleteLoggedUserWithoutCsrf() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/user/delete-logged-user");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteUserByLogin(login);
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testDeleteLoggedInAdminWhenThereIsNoSuchUser() throws Exception {
        String login = mockUser.getLogin();

        when(userService.deleteUserByLogin(login))
                .thenThrow(new NoSuchUserException());


        mockMvc.perform(get("/api/user/delete-logged-user"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testLoggedInUserWhenThereIsNoSuchUser() throws Exception {
        String login = mockUser.getLogin();

        when(userService.deleteUserByLogin(login))
                .thenThrow(new NoSuchUserException());


        mockMvc.perform(get("/api/user/delete-logged-user"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    void testDeleteLoggedUserAsUnauthenticated() throws Exception {
        var request = delete("/api/user/delete-logged-user")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());

        verify(userService, never()).deleteUserByLogin(anyString());
    }

    @Test
    @WithAnonymousUser
    void testSignUpWhenAtLeastOneFieldIsIncorrect() throws Exception {
        doThrow(new AtLeastOneFieldIncorrectException())
                .when(userService).addSignUpUserDto(mockUserDto);

        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(status().reason("AT_LEAST_ONE_FIELD_INCORRECT_EXCEPTION"));

        verify(userService).addSignUpUserDto(mockUserDto);
    }

    @Test
    @WithAnonymousUser
    void testSignUpWhenEmailIsTaken() throws Exception {
        doThrow(new EmailIsTakenException())
                .when(userService).addSignUpUserDto(mockUserDto);

        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(status().reason("EMAIL_IS_TAKEN_EXCEPTION"));

        verify(userService).addSignUpUserDto(mockUserDto);
    }

    @Test
    @WithAnonymousUser
    void testSignUpWhenLoginIsTaken() throws Exception {
        doThrow(new LoginIsTakenException())
                .when(userService).addSignUpUserDto(mockUserDto);

        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(status().reason("LOGIN_IS_TAKEN_EXCEPTION"));

        verify(userService).addSignUpUserDto(mockUserDto);
    }

    @Test
    @WithAnonymousUser
    void testSignUpByUnauthenticated() throws Exception {
        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userService).addSignUpUserDto(mockUserDto);
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testSignUpByLoggedInUser() throws Exception {
        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(userService, never()).addSignUpUserDto(any(SignUpUserDto.class));
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testSignUpByLoggedInAdmin() throws Exception {
        String content = new ObjectMapper().writeValueAsString(mockUserDto);

        var request = put("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(userService, never()).addSignUpUserDto(any(SignUpUserDto.class));
    }
}