package pl.adambalski.springbootboilerplate.controller.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.service.AdminService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {
    AutoCloseable autoCloseable;

    @InjectMocks
    AdminController adminController;

    @Mock
    AdminService adminService;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetUserDataByUUIDWhenThereIsSuchUser() {
        User user = getRandUser();
        UUID uuid = UUID.randomUUID();

        when(adminService.getUserByUUID(uuid)).thenReturn(user);

        assertEquals(user, adminController.getUserByUUID(uuid));

        verify(adminService).getUserByUUID(uuid);
    }

    @Test
    void testGetUserDataByUUIDWhenThereIsNoSuchUser() {
        NoSuchUserException noSuchUserException = new NoSuchUserException();
        UUID uuid = UUID.randomUUID();

        when(adminService.getUserByUUID(uuid)).thenThrow(noSuchUserException);

        Executable executable = () -> adminController.getUserByUUID(uuid);
        assertThrows(NoSuchUserException.class, executable);

        verify(adminService).getUserByUUID(uuid);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsSuchUser() {
        User user = getRandUser();
        String login = "login";

        when(adminService.getUserByLogin(login)).thenReturn(user);

        assertEquals(user, adminController.getUserByLogin(login));

        verify(adminService).getUserByLogin(login);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsNoSuchUser() {
        NoSuchUserException noSuchUserException = new NoSuchUserException();
        String login = "login";

        when(adminService.getUserByLogin(login)).thenThrow(noSuchUserException);

        Executable executable = () -> adminController.getUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        verify(adminService).getUserByLogin(login);
    }

    @Test
    void testDeleteUserWhenThereIsSuchUser() {
        String login = "login";

        Executable executable = () -> adminController.deleteUserByLogin(login);
        assertDoesNotThrow(executable);

        verify(adminService).deleteUserByLogin(login);
    }

    @Test
    void testDeleteUserWhenThereIsNoSuchUser() {
        NoSuchUserException noSuchUserException = new NoSuchUserException();
        String login = "login";

        doThrow(noSuchUserException).when(adminService).deleteUserByLogin(login);

        Executable executable = () -> adminController.deleteUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        verify(adminService).deleteUserByLogin(login);
    }

    private User getRandUser() {
        return new User(
                UUID.randomUUID(),
                "login",
                "Full Name",
                "e@mail.png",
                "hashed_password",
                Role.USER
        );
    }
}