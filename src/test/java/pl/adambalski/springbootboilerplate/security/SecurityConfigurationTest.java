package pl.adambalski.springbootboilerplate.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertSame;

class SecurityConfigurationTest {
    @InjectMocks
    SecurityConfiguration securityConfiguration;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void testGetUserDetailsService() {
        assertSame(userDetailsService, securityConfiguration.userDetailsService());
    }
}