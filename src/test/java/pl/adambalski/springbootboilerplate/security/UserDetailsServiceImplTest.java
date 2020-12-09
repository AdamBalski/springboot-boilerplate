package pl.adambalski.springbootboilerplate.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User(UUID.randomUUID(),
                "login", null, null, "password", Role.ADMIN);
        when(userRepository.getUserByLogin("login"))
                .thenReturn(Optional.of(user));

        UserDetails expected = user.toUserDetails();
        assertEquals(expected, userDetailsService.loadUserByUsername("login"));
    }

    @Test
    void testLoadUserByUsernameWhenUsernameDoesNotExist() {
        when(userRepository.getUserByLogin("login"))
                .thenReturn(Optional.empty());

        Executable executable = () -> userDetailsService.loadUserByUsername("login");
        assertThrows(UsernameNotFoundException.class, executable);
    }
}