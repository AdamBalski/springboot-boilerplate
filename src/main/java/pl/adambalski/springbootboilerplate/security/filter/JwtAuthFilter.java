package pl.adambalski.springbootboilerplate.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Checks for SWT (signed json web token) and sets {@link SecurityContextHolder}.<br><br>
 * When "authorization" header is not set, it passes.<br>
 * When the header is set:
 * <ul>
 *     <li>
 *         if the jwt is valid and username is found then
 *         sets {@link SecurityContextHolder}'s context to a user that has the username,
 *     </li>
 *     <li>
 *         otherwise clears {@link SecurityContextHolder}'s context;
 *     </li>
 * </ul>
 *
 * @see SecurityContextHolder
 * @see org.springframework.security.core.context.SecurityContext
 * @see javax.servlet.Filter
 * @see OncePerRequestFilter
 * @see FilterChain
 * @see JwtUtil
 * @see JwtException
 * @see UserDetails
 * @see Authentication
 * @author Adam Balski
 */
public class JwtAuthFilter extends OncePerRequestFilter {
    private final Supplier<UserDetailsService> userDetailsServiceSupplier;
    private final JwtUtil jwtUtil;

    private static final Converter<UserDetails, Authentication> userDetailsAuthenticationConverter = userDetails ->
            new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

    public JwtAuthFilter(Supplier<UserDetailsService> userDetailsServiceSupplier, JwtUtil jwtUtil) {
        this.userDetailsServiceSupplier = userDetailsServiceSupplier;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserDetailsService userDetailsService = userDetailsServiceSupplier.get();

        String token = request.getHeader("authorization");
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.clearContext();
        try {
            // throws JwtException when not valid
            Claims claims = jwtUtil.verifyAndGetClaims(token);
            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = userDetailsAuthenticationConverter.convert(userDetails);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | UsernameNotFoundException exception) {
            // pass (SecurityContext is cleared)
        }

        filterChain.doFilter(request, response);
    }
}
