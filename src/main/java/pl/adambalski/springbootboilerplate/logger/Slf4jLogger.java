package pl.adambalski.springbootboilerplate.logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Slf4j implementation of {@link Logger}<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.logger.Logger
 * @author Adam Balski
 */
@Component
public class Slf4jLogger implements Logger {
    @SuppressWarnings("rawtypes")
    @Override
    public void log(String string, Class source, Status status) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(source);

        switch (status) {
            case INFO -> logger.info(string);
            case EXCEPTION -> logger.error(string);
            case DEBUG -> logger.debug(string);
        }
    }
}
