package pl.adambalski.springbootboilerplate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Factory that creates beans by {@link #passwordEncoderBean()}, which returns a bean of type {@link org.springframework.security.crypto.password.PasswordEncoder}.<br>
 * The encoder is a {@link BCryptPasswordEncoder} with strength of 10.<br><br>
 *
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see BCryptPasswordEncoder
 * @author Adam Balski
 */
@Configuration
public class PasswordEncoderFactory {
    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder(10);
    }
}
