package ru.yandex.potapov.schedule.manager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file = new File("resources/saveFileTest.csv");

    protected FileBackedTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    protected TaskManager createNewManager() {
        return new FileBackedTasksManager();
    }

    @Test
    void loadFromFileWithoutHistory() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager.getHistory(), manager.getHistory(), "History lists are not equals");
        assertEquals(taskManager.getTasks(), manager.getTasks(), "Task lists are not equals");
        assertEquals(taskManager.getEpics(), manager.getEpics(), "Epic lists are not equals");
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks(), "Subtask lists are not equals");
    }

    @Test
    void loadFromFileWithoutSubtasks() {
        taskManager.deleteSubtasks();
        TaskManager manager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager.getHistory(), manager.getHistory(), "History lists are not equals");
        assertEquals(taskManager.getTasks(), manager.getTasks(), "Task lists are not equals");
        assertEquals(taskManager.getEpics(), manager.getEpics(), "Epic lists are not equals");
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks(), "Subtask lists are not equals");
    }
}
