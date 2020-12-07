package pl.adambalski.springbootboilerplate.security.filter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {
    // Components
    private FilterChain filterChain;
    private Filter filter;
    private UserDetailsService userDetailsService;

    // Data
    private String validToken;
    private String notValidToken;
    private String usernameInValidToken;
    private UserDetails userDetailsInToken;

    @BeforeEach
    void init() {
        // Data used in tests
        JwtUtil jwtUtil = new JwtUtil(Keys.secretKeyFor(SignatureAlgorithm.HS512));

        usernameInValidToken = "username1";
        userDetailsInToken = new User(usernameInValidToken, "password1", List.of());
        notValidToken = "this is not a valid token";
        validToken = jwtUtil
                .tokenOf(usernameInValidToken);



        // Components used in tests
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthFilter(() -> userDetailsService, jwtUtil);
        filterChain = mock(FilterChain.class);

        // Reset SecurityContext
        SecurityContextHolder.clearContext();
    }

    @Test
    void testFilterWhenThereIsNoJwtAndSecurityContextIsNotNull() throws IOException, ServletException {
        setAuthenticationToCertainUser();

        HttpServletRequest request = getEmptyRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        // AbstractAuthenticationToken overrides Object.equals(Object)
        assertEquals(getCertainUser(), SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFilterWhenThereIsNoJwtAndSecurityContextIsNull() throws IOException, ServletException {
        HttpServletRequest request = getEmptyRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFilterWhenThereIsInvalidJwtAndSecurityContextIsNotNull() throws IOException, ServletException {
        setAuthenticationToCertainUser();

        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(notValidToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFilterWhenThereIsInvalidJwtAndSecurityContextIsNull() throws IOException, ServletException {
        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(notValidToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFilterWhenThereIsValidJwtAndSecurityContextIsNullAndUserExists() throws IOException, ServletException {
        when(userDetailsService.loadUserByUsername(usernameInValidToken))
                .thenReturn(userDetailsInToken);

        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(validToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(
                convertUserDetailsToAuthentication(userDetailsInToken),
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void testFilterWhenThereIsValidJwtAndSecurityContextIsNullAndUserDoesNotExist() throws IOException, ServletException {
        when(userDetailsService.loadUserByUsername(usernameInValidToken))
                .thenThrow(UsernameNotFoundException.class);

        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(validToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFilterWhenThereIsValidJwtAndSecurityContextIsNotNullAndUserExists() throws IOException, ServletException {
        setAuthenticationToCertainUser();

        when(userDetailsService.loadUserByUsername(usernameInValidToken))
                .thenReturn(userDetailsInToken);

        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(validToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(
                convertUserDetailsToAuthentication(userDetailsInToken),
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void testFilterWhenThereIsValidJwtAndSecurityContextIsNotNullAndUserDoesNotExist() throws IOException, ServletException {
        setAuthenticationToCertainUser();

        when(userDetailsService.loadUserByUsername(usernameInValidToken))
                .thenThrow(UsernameNotFoundException.class);

        HttpServletRequest request = getHttpServletRequestWithAuthorizationHeader(validToken);
        HttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void setAuthenticationToCertainUser() {
        SecurityContextHolder.getContext().setAuthentication(getCertainUser());
    }

    private UsernamePasswordAuthenticationToken getCertainUser() {
        return new UsernamePasswordAuthenticationToken("username2", "password2");
    }


    private HttpServletRequest getHttpServletRequestWithAuthorizationHeader(String authorization) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("authorization"))
                .thenReturn(authorization);

        return request;
    }

    private HttpServletRequest getEmptyRequest() {
        return new MockHttpServletRequest();
    }

    private Authentication convertUserDetailsToAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }
}
