package ru.yandex.potapov.schedule.manager;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    protected HttpTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    protected TaskManager createNewManager() {
        return Managers.getDefault();
    }

    @Test
    void loadFromServer() {
        TaskManager manager = new HttpTaskManager("http://localhost:8078").loadFromServer();
        assertEquals(taskManager.getHistory(), manager.getHistory(), "History lists are not equals");
        assertEquals(taskManager.getTasks(), manager.getTasks(), "Task lists are not equals");
        assertEquals(taskManager.getEpics(), manager.getEpics(), "Epic lists are not equals");
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks(), "Subtask lists are not equals");
    }
}
