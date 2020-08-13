package pl.adambalski.springbootboilerplate.logger;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@SuppressWarnings("rawtypes")
@Component
public interface Logger {
    default void error(Exception e, Class source) {
        StringBuilder sb = new StringBuilder(e.getMessage());

        Arrays.stream(e.getStackTrace())
                .map(stackTraceElement -> "\n" + stackTraceElement.toString())
                .forEach(sb::append);

        error(sb, source);
    }

    default void log(Object obj, Class source) {
        log(obj, source, Status.INFO);
    }

    default void debug(Object obj, Class source) {
        log(obj, source, Status.DEBUG);
    }

    default void error(Object obj, Class source) {
        log(obj, source, Status.EXCEPTION);
    }

    default void log(Object obj, Class source, Status status) {
        log(obj.toString(), source, status);
    }

    void log(String string, Class source, Status status);
}
