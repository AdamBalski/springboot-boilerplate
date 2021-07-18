package pl.adambalski.springbootboilerplate.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.adambalski.springbootboilerplate.security.filter.JwtAuthFilter;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

import javax.crypto.SecretKey;
import javax.servlet.Filter;

/**
 * Security configuration.
 *
 * @see WebSecurityConfigurerAdapter
 * @see Filter
 * @author Adam Balski
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfiguration() {
        this.jwtUtil = new JwtUtil(KEY);
    }

    @Autowired
    private void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieHttpOnly(false);

        HttpStatusEntryPoint http401StatusEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

        http
                // CORS
                .cors().and()
                // CSRF
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository)
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(
                                        "/api/user/authenticate",
                                        HttpMethod.POST.name()),
                                new AntPathRequestMatcher(
                                        "/api/user/sign-up",
                                        HttpMethod.PUT.name()
                                )).and()
                // Sessions
                .sessionManagement().disable()
                // 401 instead of 403 for unauthenticated people
                .exceptionHandling()
                    .authenticationEntryPoint(http401StatusEntryPoint).and()
                // Filters
                .addFilterAfter(new JwtAuthFilter(this::userDetailsService, jwtUtil), AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
