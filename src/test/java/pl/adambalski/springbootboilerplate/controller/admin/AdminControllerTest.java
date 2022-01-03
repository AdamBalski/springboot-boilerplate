package pl.adambalski.springbootboilerplate.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;
import pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;
import pl.adambalski.springbootboilerplate.service.AdminService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminController adminController;

    @MockBean
    AdminService adminService;

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

    User mockUser;

    AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        SignUpUserDto mockUserDto = new SignUpUserDto(
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
    @WithMockUser(roles = "ADMIN")
    void testGetUserByUuidByAdmin() throws Exception {
        UUID uuid = mockUser.getUuid();

        when(adminService.getUserByUUID(uuid))
                .thenReturn(mockUser);

        mockUser.setPassword(null);
        String expected = new ObjectMapper().writeValueAsString(mockUser);

        mockMvc.perform(get("/api/admin/get-user-by-uuid/?uuid=" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByUuidByAdminWhenUserDoesNotExist() throws Exception {
        UUID uuid = mockUser.getUuid();

        when(adminService.getUserByUUID(uuid))
                .thenThrow(new NoSuchUserException());

        mockMvc.perform(get("/api/admin/get-user-by-uuid/?uuid=" + uuid))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("NO_SUCH_USER_EXCEPTION"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserByUuidByUser() throws Exception {
        UUID uuid = mockUser.getUuid();

        mockMvc.perform(get("/api/admin/get-user-by-uuid/?uuid=" + uuid))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithAnonymousUser
    void testGetUserByUuidByUnauthenticated() throws Exception {
        UUID uuid = mockUser.getUuid();

        mockMvc.perform(get("/api/admin/get-user-by-uuid/?uuid=" + uuid))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByLoginByAdmin() throws Exception {
        String login = mockUser.getLogin();

        when(adminService.getUserByLogin(login))
                .thenReturn(mockUser);

        String expected =
                """
                    {
                        "login":"%s"
                    }
                """.formatted(login);

        mockMvc.perform(get("/api/admin/get-user-by-login/?login=" + login))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByLoginByAdminWhenUserDoesNotExist() throws Exception {
        String login = mockUser.getLogin();

        when(adminService.getUserByLogin(login))
                .thenThrow(new NoSuchUserException());

        mockMvc.perform(get("/api/admin/get-user-by-login/?login=" + login))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("NO_SUCH_USER_EXCEPTION"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserByLoginByUser() throws Exception {
        String login = mockUser.getLogin();

        mockMvc.perform(get("/api/admin/get-user-by-login/?login=" + login))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testGetUserByLoginByUnAuthenticated() throws Exception {
        String login = mockUser.getLogin();

        mockMvc.perform(get("/api/admin/get-user-by-login/?login=" + login))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserByLoginByAdmin() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/admin/delete-user")
                .with(csrf())
                .content(login);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(adminService).deleteByLogin(login);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserByLoginWithoutCsrf() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/admin/delete-user")
                .content(login);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(adminService, never()).deleteByLogin(login);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserByLoginByAdminWhenThereIsNoSuchUser() throws Exception {
        String login = mockUser.getLogin();

        doThrow(new NoSuchUserException())
                .when(adminService).deleteByLogin(login);

        var request = delete("/api/admin/delete-user")
                .with(csrf())
                .content(login);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(status().reason("NO_SUCH_USER_EXCEPTION"));

        verify(adminService).deleteByLogin(login);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteUserByLoginByUser() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/admin/delete-user")
                .with(csrf())
                .content(login);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(adminService, never()).deleteByLogin(login);

    }

    @Test
    @WithAnonymousUser
    void testDeleteUserByLoginByAnonymous() throws Exception {
        String login = mockUser.getLogin();

        var request = delete("/api/admin/delete-user")
                .content(login)
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());

        verify(adminService, never()).deleteByLogin(login);
    }
}
