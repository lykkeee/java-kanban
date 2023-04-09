package ru.yandex.potapov.schedule.manager;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }

    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
