package pl.adambalski.springbootboilerplate.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

/**
 * Contains a function responsible for authenticating a user, which returns a jwt token.<br><br>
 *
 * @see SecurityConfiguration
 * @see UserDetailsService
 * @see UserDetails
 * @see JwtUtil
 * @see PasswordEncoder
 * @author Adam Balski
 */

@ComponentScan("pl.adambalski.springbootboilerplate")
@RestController
@CrossOrigin
public class AuthenticationController {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = new JwtUtil(SecurityConfiguration.KEY);
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/api/user/authenticate")
    @PreAuthorize(value = "permitAll()")
    public JwtTokenDto authenticate(@RequestBody LoginDto loginDto) {
        String username = loginDto.username();
        String password = loginDto.password();

        // 3 lines below can throw HttpStatus.BAD_REQUEST (HTTP 400) or HTTPStatus.UNAUTHORIZED (HTTP 401)
        checkIfCredentialsAreNull(username, password);
        UserDetails userDetails = loadUserDetails(username);
        checkIfPasswordMatches(userDetails, password);

        return getJwtTokenDto(username);
    }

    private void checkIfCredentialsAreNull(String username, String password) {
        if(username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private UserDetails loadUserDetails(String username) {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private void checkIfPasswordMatches(UserDetails userDetails, String password) {
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private JwtTokenDto getJwtTokenDto(String username) {
        return new JwtTokenDto(jwtUtil.tokenOf(username));
    }
}
