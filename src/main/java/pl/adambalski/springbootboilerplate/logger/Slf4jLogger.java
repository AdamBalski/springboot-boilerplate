package pl.adambalski.springbootboilerplate.logger;

import org.slf4j.LoggerFactory;

/**
 * Slf4j implementation of {@link Logger}<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.logger.Logger
 * @author Adam Balski
 */
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
