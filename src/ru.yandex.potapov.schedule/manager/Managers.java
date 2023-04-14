package ru.yandex.potapov.schedule.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/saveFile.csv"));
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTasksManager(File file) {
        return new FileBackedTasksManager(new File(String.valueOf(file)));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
