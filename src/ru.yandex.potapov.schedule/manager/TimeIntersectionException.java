package ru.yandex.potapov.schedule.manager;

public class TimeIntersectionException extends RuntimeException{
    public TimeIntersectionException() {
    }

    public TimeIntersectionException(final String message) {
        super(message);
    }

    public TimeIntersectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
