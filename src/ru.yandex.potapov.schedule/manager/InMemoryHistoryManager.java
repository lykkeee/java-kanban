package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
