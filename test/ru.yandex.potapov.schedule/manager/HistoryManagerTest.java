package ru.yandex.potapov.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Status;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    protected static final HistoryManager historyManager = Managers.getDefaultHistory();
    protected static Task task;
    protected static Epic epic;
    protected static Subtask subtask;

    @BeforeAll
    public static void beforeAll() {
        task = new Task("task1", "taskDescription", Status.NEW, 0, 11, LocalDateTime.of(2022, 1, 1, 1, 1));
        epic = new Epic("epic1", "epicDescription", Status.NEW, 1, new ArrayList<>(), 11,
                LocalDateTime.of(2022, 1, 1, 1, 1), LocalDateTime.of(2205, 1, 1, 1, 1));
        subtask = new Subtask("subtask1", "subtaskDescription", Status.NEW, 2, 1, 15, LocalDateTime.of(2022, 1, 1, 1, 1));
    }
    @Test
    void add() {
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is not empty");

        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "History is empty");
        assertEquals(1, history.size(), "History size is not equals");

        historyManager.add(task);
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "History is empty");
        assertEquals(1, history.size(), "History size is not equals");
    }

    @Test
    void remove() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Incorrect history size");

        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(2, history.size(), "Incorrect history size");

        historyManager.add(task);
        historyManager.remove(subtask.getId());
        assertEquals(2, history.size(), "Incorrect history size");
    }
}
