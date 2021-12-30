package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.NoSuchRefreshTokenException;
import pl.adambalski.springbootboilerplate.exception.NoSuchUsernameAuthenticationException;
import pl.adambalski.springbootboilerplate.exception.PasswordDoesNotMatchException;
import pl.adambalski.springbootboilerplate.logger.Logger;
import pl.adambalski.springbootboilerplate.logger.Status;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;
import pl.adambalski.springbootboilerplate.util.RandomAlphaNumericStringGenerator;

/**
 * Performs main logic of operations like registering, authentication and deletion of an account.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @see JwtUtil
 * @see RefreshTokenRepository
 * @see UserDetailsService
 */
@Service
public class AuthenticationService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RandomAlphaNumericStringGenerator randomAlphaNumericStringGenerator;
    private final Logger logger;


    @Autowired
    public AuthenticationService(RefreshTokenRepository refreshTokenRepository,
                                 UserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 RandomAlphaNumericStringGenerator randomAlphaNumericStringGenerator,
                                 @Qualifier("slf4jLogger") Logger logger) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = new JwtUtil(SecurityConfiguration.KEY);
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.randomAlphaNumericStringGenerator = randomAlphaNumericStringGenerator;
        this.logger = logger;
    }

    @Scheduled(fixedRate = 24 * 3600 * 1000) // one time each day
    public void deleteAllExpired() {
        refreshTokenRepository.deleteAllByExpirationDateBeforeNow();
        logger.log("DELETED ALL EXPIRED", AuthenticationService.class, Status.INFO);
    }

    public RefreshToken authenticate(LoginDto loginDto) {
        String username = loginDto.username();
        String password = loginDto.password();

        // throws AtLeastOneFieldIncorrectException if username or password is null
        checkIfCredentialsAreNull(username, password);
        // throws spring security's UsernameNotFoundException if user does not exist
        UserDetails userDetails = loadUserDetails(username);
        // throws PasswordDoesNotMatchException if password is not correct
        checkIfPasswordMatches(userDetails, password);

        RefreshToken refreshToken = RefreshToken.createRefreshToken(loginDto.username(), randomAlphaNumericStringGenerator);
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public JwtTokenDto refresh(String username, String refreshTokenValue) {
        if(refreshTokenRepository.existsByUserLoginAndToken(username, refreshTokenValue)) {
            return getJwtTokenDto(username);
        }
        throw new NoSuchRefreshTokenException();
    }

    private void checkIfCredentialsAreNull(String username, String password) {
        if (username == null || password == null) {
            throw new AtLeastOneFieldIncorrectException();
        }
    }

    private UserDetails loadUserDetails(String username) throws NoSuchRefreshTokenException {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch(UsernameNotFoundException e) {
            throw new NoSuchUsernameAuthenticationException();
        }
    }

    private void checkIfPasswordMatches(UserDetails userDetails, String password) {
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new PasswordDoesNotMatchException();
        }
    }

    private JwtTokenDto getJwtTokenDto(String username) {
        return new JwtTokenDto(jwtUtil.tokenOf(username));
    }
}
