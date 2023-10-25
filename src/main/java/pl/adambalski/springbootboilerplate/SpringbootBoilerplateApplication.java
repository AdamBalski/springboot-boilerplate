package pl.adambalski.springbootboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.adambalski.springbootboilerplate.logger.Logger;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

import java.util.Base64;
import java.util.Objects;

/**
 * Class that starts the server.<br><br>
 *
 * @author Adam Balski
 */
@SpringBootApplication
public class SpringbootBoilerplateApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringbootBoilerplateApplication.class, args);
        Logger logger = applicationContext.getBean("slf4jLogger", Logger.class);

        String isDebug = applicationContext.getEnvironment().getProperty("APP_DEVELOPMENT_PROFILE");
        if(isDebug != null && isDebug.equalsIgnoreCase("true")) {
            logJwtKey(logger);
        }
    }

    private static void logJwtKey(Logger logger) {
        String key = Base64.getEncoder().encodeToString(SecurityConfiguration.KEY.getEncoded());
        logger.log("Key used to make JWTs: " + key, SpringbootBoilerplateApplication.class);
    }
}
