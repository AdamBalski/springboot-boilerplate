package pl.adambalski.springbootboilerplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures CORS policy.<br><br>
 *
 * @see WebMvcConfigurer
 * @author Adam Balski
 */
@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    private String origins;
    private long maxAge;
    private String[] allowedMethods;

    @Autowired
    private void setAllowedMethods(@Value(value = "${app.security.cors.allowed_methods}") String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    @Autowired
    private void setOrigins(@Value(value = "${app.security.cors.origins}") String origins) {
        this.origins = origins;
    }

    @Autowired
    private void setMaxAge(@Value(value = "${app.security.cors.max_age}") long maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowCredentials(false)
                .allowedMethods(allowedMethods)
                .allowedOrigins(origins)
                .maxAge(maxAge);

        registry.addMapping("/api/auth/authenticate")
                .allowCredentials(true)
                .allowedMethods("POST")
                .allowedOrigins(origins)
                .maxAge(maxAge);
    }
}
