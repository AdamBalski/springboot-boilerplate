package pl.adambalski.springbootboilerplate.security.mockmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.security.GrantedAuthorityImpl;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest
public class MvcAndSecurityConfigurationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationContext applicationContext;

    // ApplicationContext wants repositories (controllers -> services -> repositories),
    // to inject to services, which are needed to be injected to controllers
    // but springboot can't instantiate repositories with @WebMvcTest, so we
    // have to create mock beans.
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private UserRepository userRepository;

    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        jwtUtil = new JwtUtil(SecurityConfiguration.KEY);
        mockUserDetailsService();
    }

    private void mockUserDetailsService() {
        UserDetailsService userDetailsService = new MockUserDetailsService();

        SecurityConfiguration securityConfiguration = applicationContext.getBean(SecurityConfiguration.class);
        ReflectionTestUtils.setField(securityConfiguration, "userDetailsService", userDetailsService);
    }

    private void testByParameters(String path, UserType testBy, ResultMatcher httpStatusMatcher, String expectedResult) {
        try {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .get(path);

            // if authenticated, then set jwt token
            if (testBy != UserType.UNAUTHENTICATED) {
                request = request.header("authorization", getValidJwt(testBy));
            }

            mvc.perform(request)
                    .andExpect(httpStatusMatcher)
                    .andExpect(content().string(expectedResult));
        } catch (Exception e) {
            fail(e);
        }
    }

    private String getValidJwt(UserType userType) {
        return jwtUtil.tokenOf(userType.name());
    }

    // Accessible by All
    @Test
    void testAccessibleByAllByUnauthenticated() {
        testByParameters("/mock/all", UserType.UNAUTHENTICATED, MockMvcResultMatchers.status().isOk(), "all");
    }

    @Test
    void testAccessibleByAllByUser() {
        testByParameters("/mock/all", UserType.USER, MockMvcResultMatchers.status().isOk(), "all");
    }

    @Test
    void testAccessibleByAllByAdmin() {
        testByParameters("/mock/all", UserType.ADMIN, MockMvcResultMatchers.status().isOk(), "all");
    }

    // Accessible only by User (not by admin)
    @Test
    void testAccessibleOnlyByUserByUnauthenticated() {
        testByParameters("/mock/user", UserType.UNAUTHENTICATED, MockMvcResultMatchers.status().isForbidden(), "");
    }

    @Test
    void testAccessibleOnlyByUserByUser() {
        testByParameters("/mock/user", UserType.USER, MockMvcResultMatchers.status().isOk(), "user");
    }

    @Test
    void testAccessibleOnlyByUserByAdmin() {
        testByParameters("/mock/user", UserType.ADMIN, MockMvcResultMatchers.status().isForbidden(), "");
    }

    // Accessible by user and admin
    @Test
    void testAccessibleByUserAndAdminByUnauthenticated() {
        testByParameters("/mock/user-and-admin", UserType.UNAUTHENTICATED, MockMvcResultMatchers.status().isForbidden(), "");
    }

    @Test
    void testAccessibleByUserAndAdminByUser() {
        testByParameters("/mock/user-and-admin", UserType.USER, MockMvcResultMatchers.status().isOk(), "user || admin");
    }

    @Test
    void testAccessibleByUserAndAdminByAdmin() {
        testByParameters("/mock/user-and-admin", UserType.ADMIN, MockMvcResultMatchers.status().isOk(), "user || admin");
    }

    // Accessible by admin
    @Test
    void testAccessibleByAdminByUnauthenticated() {
        testByParameters("/mock/admin", UserType.UNAUTHENTICATED, MockMvcResultMatchers.status().isForbidden(), "");
    }

    @Test
    void testAccessibleByAdminByUser() {
        testByParameters("/mock/admin", UserType.USER, MockMvcResultMatchers.status().isForbidden(), "");
    }

    @Test
    void testAccessibleByAdminByAdmin() {
        testByParameters("/mock/admin", UserType.ADMIN, MockMvcResultMatchers.status().isOk(), "admin");
    }

    private static class MockUserDetailsService implements UserDetailsService {
        private final UserDetails user = new User(
                "USER",
                "12345678",
                List.of(new GrantedAuthorityImpl(Role.USER))
        );
        private final UserDetails admin = new User(
                "ADMIN",
                "12345678",
                List.of(new GrantedAuthorityImpl(Role.ADMIN))
        );

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            if ("USER".equals(s)) return user;
            if ("ADMIN".equals(s)) return admin;

            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
    }

    // For example 'UNAUTHENTICATED' User can get 200OK only from '/mock/all', but
    // 'USER' can from '/mock/user' too
    // 'ADMIN' can get 200OK from all j's methods ('/mock/all', '/mock/user', '/mock/admin')
    private enum UserType {
        UNAUTHENTICATED,
        USER,
        ADMIN
    }
}

@SuppressWarnings("unused")
@RestController
class MockController {
    @PreAuthorize(value = "permitAll()")
    @GetMapping(value = "/mock/all")
    public String accessibleByAll() {
        return "all";
    }

    @PreAuthorize(value = "hasAnyRole('USER')")
    @GetMapping(value = "/mock/user")
    public String accessibleOnlyByUser() {
        return "user";
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = "/mock/user-and-admin")
    public String accessibleByUserAndAdmin() {
        return "user || admin";
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping(value = "/mock/admin")
    public String accessibleByAdmin() {
        return "admin";
    }

}