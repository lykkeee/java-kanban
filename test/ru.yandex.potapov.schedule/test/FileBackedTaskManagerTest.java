package ru.yandex.potapov.schedule.test;

import org.junit.jupiter.api.Test;
import ru.yandex.potapov.schedule.manager.FileBackedTasksManager;
import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file = new File("resources/saveFileTest.csv");

    @Override
    protected TaskManager createNewManager() {
        return Managers.getFileBackedTasksManager(new File("resources/saveFileTest.csv"));
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
