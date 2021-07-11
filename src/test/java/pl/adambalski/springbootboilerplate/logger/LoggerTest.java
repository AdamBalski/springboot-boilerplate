package pl.adambalski.springbootboilerplate.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class LoggerTest {
    AutoCloseable autoCloseable;

    @Mock
    Logger logger;

    // object.toString() returns "toString"
    Object obj;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        logger = Mockito.mock(Logger.class);

        obj = new Object() {
            @Override
            public String toString() {
                return "toString";
            }
        };


//         setting up the mocked logger
        doCallRealMethod()
                .when(logger)
                .log(any(Object.class), eq(LoggerTest.class), any(Status.class));

        doCallRealMethod()
                .when(logger)
                .log(any(Object.class), eq(LoggerTest.class));
        doCallRealMethod()
                .when(logger)
                .debug(any(Object.class), eq(LoggerTest.class));
        doCallRealMethod()
                .when(logger)
                .error(any(Object.class), eq(LoggerTest.class));
        doCallRealMethod()
                .when(logger)
                .error(any(Exception.class), eq(LoggerTest.class));
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    // tests "default void log(Object obj, Class source)"
    @Test
    void testLogInfo() {
        logger.log(obj, LoggerTest.class);

        String string = "toString";

        verify(logger)
                .log(
                        eq(string),
                        eq(LoggerTest.class),
                        eq(Status.INFO)
                );
    }

    @Test
    void testDebug() {
        logger.debug(obj, LoggerTest.class);

        String string = "toString";

        verify(logger)
                .log(
                        eq(string),
                        eq(LoggerTest.class),
                        eq(Status.DEBUG)
                );
    }

    @Test
    void testExceptionWithObj() {
        logger.error(obj, LoggerTest.class);

        String string = "toString";

        verify(logger)
                .log(
                        eq(string),
                        eq(LoggerTest.class),
                        eq(Status.EXCEPTION)
                );
    }

    @Test
    void testErrorWithException() {
        Exception exception = new Exception("Message");

        // Sets stack trace so there's only one element
        StackTraceElement firstStackTraceElement = exception.getStackTrace()[0];
        exception.setStackTrace(new StackTraceElement[]{firstStackTraceElement});

        logger.error(exception, LoggerTest.class);

        //language=RegExp
        String regex =
                """
                Message
                pl\\.adambalski\\.springbootboilerplate\\.logger\\.LoggerTest\\.testErrorWithException\\(LoggerTest\\.java:[0-9]+\\)""";

        verify(logger)
                .log(
                        matches(regex),
                        eq(LoggerTest.class),
                        eq(Status.EXCEPTION)
                );
    }

    // tests "default void log(Object obj, Class source, Status status)"
    @Test
    void testLog() {
        logger.log("String", LoggerTest.class, Status.INFO);

        String string = "String";

        verify(logger)
                .log(
                        eq(string),
                        eq(LoggerTest.class),
                        eq(Status.INFO)
                );
    }
}