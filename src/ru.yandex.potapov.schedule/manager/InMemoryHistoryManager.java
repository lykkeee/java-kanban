package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    public final List<Task> history = new ArrayList<>();
    private final int maxListSize = 10;

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > maxListSize) {
            history.remove(0);
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
