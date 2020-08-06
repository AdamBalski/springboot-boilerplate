package com.sellit.sellit.logger;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StdoutLoggerImpl implements Logger {
    @SuppressWarnings("rawtypes")
    @Override
    public void log(String string, Class source, Status status) {
        System.out.printf(
                "%s [%s] %s : %s",
                new Date(),
                status,
                source,
                string
        );
    }
}
