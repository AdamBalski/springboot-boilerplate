package pl.adambalski.springbootboilerplate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * {@link #passwordEncoderBean()} returns a bean of {@link org.springframework.security.crypto.password.PasswordEncoder}<br><br>
 *
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @author Adam Balski
 */
public class PasswordEncoder {
    @Bean("passwordEncoder")
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder(10);
    }
}
