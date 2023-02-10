package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}