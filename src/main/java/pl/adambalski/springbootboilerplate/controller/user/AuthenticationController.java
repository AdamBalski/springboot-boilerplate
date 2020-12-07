package pl.adambalski.springbootboilerplate.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;

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
    public ResponseEntity<String> authenticate(@RequestBody LoginDto loginDto) {
        String username = loginDto.username();
        String password = loginDto.password();

        if(username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        JwtTokenDto token = new JwtTokenDto(jwtUtil.tokenOf(username));

        return new ResponseEntity<>(
                token.toJson(),
                HttpStatus.OK);
    }
}
