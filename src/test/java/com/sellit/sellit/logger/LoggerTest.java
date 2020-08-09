package com.sellit.sellit.logger;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoggerTest {
    @Test
    void testErrorWithException() {
        Exception exception = new Exception("Message");

        // Sets stack trace so there's only one element
        StackTraceElement firstStackTraceElement = exception.getStackTrace()[0];
        exception.setStackTrace(new StackTraceElement[]{firstStackTraceElement});

        Logger logger = mock(Logger.class);
        doCallRealMethod()
                .when(logger)
                .error(exception, LoggerTest.class);
        doCallRealMethod()
                .when(logger)
                .error(any(StringBuilder.class), eq(LoggerTest.class));
        doCallRealMethod()
                .when(logger)
                .log(any(StringBuilder.class), eq(LoggerTest.class), eq(Status.EXCEPTION));

        logger.error(exception, LoggerTest.class);

        // Check if a method we wanted to be called was actually called
        //language=RegExp
        String regex = "Message\n" +
                "com\\.sellit\\.sellit\\.logger\\.LoggerTest\\.testErrorWithException\\(LoggerTest\\.java:[0-9]+\\)";

        verify(logger)
                .log(
                        matches(regex),
                        eq(LoggerTest.class),
                        eq(Status.EXCEPTION)
                );
    }
}