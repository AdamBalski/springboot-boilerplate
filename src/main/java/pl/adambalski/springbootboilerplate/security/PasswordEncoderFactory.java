package pl.adambalski.springbootboilerplate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@link #passwordEncoderBean()} returns a bean of {@link org.springframework.security.crypto.password.PasswordEncoder}<br><br>
 *
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @author Adam Balski
 */
@Configuration
public class PasswordEncoderFactory {
    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder(10);
    }
}
