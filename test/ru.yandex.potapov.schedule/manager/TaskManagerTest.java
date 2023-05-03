package ru.yandex.potapov.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Status;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager = (T) createNewManager();
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected TaskManagerTest() throws IOException, InterruptedException {
    }

    protected TaskManager createNewManager() throws IOException, InterruptedException {
        return Managers.getDefault();
    }

    @BeforeEach
    public void beforeEach() {
        task = new Task("task1", "taskDescription", Status.NEW, 0, 11, LocalDateTime.of(2022, 1, 1, 1, 1));
        epic = new Epic("epic1", "epicDescription", Status.NEW, 0, new ArrayList<>(), 11,
                LocalDateTime.of(2022, 1, 1, 1, 1), LocalDateTime.of(2205, 1, 1, 1, 1));
        subtask = new Subtask("subtask1", "subtaskDescription", Status.NEW, 0, 1, 15, LocalDateTime.of(2022, 8, 1, 1, 1));
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
    }

    @Test
    void addNewTask() {
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks are not equal.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Tasks are not returned.");
        assertEquals(1, tasks.size(), "Incorrect number of tasks.");
        assertEquals(task, tasks.get(0), "Tasks are not equal.");
    }

    @Test
    void addNewEpic() {
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics are not equal.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Epics are not returned.");
        assertEquals(1, epics.size(), "Incorrect number of epics.");
        assertEquals(epic, epics.get(0), "Epics are not equal.");
    }

    @Test
    void addNewSubtask() {
        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Subtask not found.");
        assertEquals(subtask, savedSubtask, "Subtasks are not equal.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Subtasks are not returned.");
        assertEquals(1, subtasks.size(), "Incorrect number of subtasks.");
        assertEquals(subtask, subtasks.get(0), "Subtasks are not equal.");
        assertEquals(subtask.getEpicId(), epic.getId(), "Epic ids are not equal");
    }

    @Test
    void getTasks() {
        final List<Task> taskList = taskManager.getTasks();
        assertNotNull(taskList, "Task list is empty");
        assertEquals(1, taskList.size(), "Incorrect number of tasks in task list");
    }

    @Test
    void getEpics() {
        final List<Epic> epicList = taskManager.getEpics();
        assertNotNull(epicList, "Epic list is empty");
        assertEquals(1, epicList.size(), "Incorrect number of epics in epic list");
    }

    @Test
    void getSubtasks() {
        final List<Subtask> subtaskList = taskManager.getSubtasks();
        assertNotNull(subtaskList, "Subtask list is empty");
        assertEquals(1, subtaskList.size(), "Incorrect number of subtasks in subtask list");
    }

    @Test
    void deleteTasks() {
        taskManager.deleteTasks();
        final List<Task> taskList = taskManager.getTasks();
        assertEquals(0, taskList.size(), "Incorrect number of tasks in task list");
    }

    @Test
    void deleteEpics() {
        taskManager.deleteEpics();
        final List<Epic> epicList = taskManager.getEpics();
        final List<Subtask> subtaskList = taskManager.getSubtasks();
        assertEquals(0, epicList.size(), "Incorrect number of epics in epic list");
        assertEquals(0, subtaskList.size(), "Incorrect number of subtasks in subtask list");
    }

    @Test
    void deleteSubtasks() {
        final int epicId = epic.getId();
        taskManager.deleteSubtasks();
        final List<Subtask> subtaskList = taskManager.getSubtasks();
        assertEquals(0, taskManager.getEpic(epicId).getSubtasks().size(), "Incorrect number of subtasks in subtask list in epic");
        assertEquals(0, subtaskList.size(), "Incorrect number of subtasks in subtask list");
        assertEquals(taskManager.getEpic(epicId).getStatus(), Status.NEW, "Incorrect epic status");
    }

    @Test
    void getTask() {
        taskManager.getTask(task.getId());
        assertNotNull(taskManager.getHistory(), "History is empty");
        assertEquals(1, taskManager.getHistory().size(), "Incorrect history list size");
    }

    @Test
    void getEpic() {
        taskManager.getEpic(epic.getId());
        assertNotNull(taskManager.getHistory(), "History is empty");
        assertEquals(1, taskManager.getHistory().size(), "Incorrect history list size");
    }

    @Test
    void getSubtask() {
        taskManager.getSubtask(subtask.getId());
        assertNotNull(taskManager.getHistory(), "History is empty");
        assertEquals(1, taskManager.getHistory().size(), "Incorrect history list size");
    }

    @Test
    void getSubtaskByEpic() {
        final int epicId = epic.getId();
        final int subtaskId = subtask.getId();
        final Subtask subtask1 = taskManager.getSubtaskByEpic(subtaskId, epicId);
        assertNotNull(subtask1, "Subtask is empty");
        assertEquals(subtask1, subtask, "Subtasks are not equal");
    }

    @Test
    void updateTask() {
        final int taskId = task.getId();
        final Task oldTask = taskManager.getTask(taskId);
        final Task newTask = new Task("task1_update", "taskDescription_update", Status.NEW, 0,
                3, LocalDateTime.of(2022, 3, 2, 2, 4));
        taskManager.updateTask(newTask);
        assertNotEquals(taskManager.getTask(taskId), oldTask, "Tasks are equal");
        assertEquals(taskManager.getTask(taskId), newTask, "Tasks are not equal");
    }

    @Test
    void updateEpic() {
        final int epicId = epic.getId();
        final Epic oldEpic = taskManager.getEpic(epicId);
        final Epic newEpic = new Epic("epic1_update", "epicDescription_update", Status.NEW, epicId,
                new ArrayList<>(), 5, LocalDateTime.of(2018, 1, 1, 1, 1), LocalDateTime.of(2019, 1, 1, 1, 1));
        taskManager.updateEpic(newEpic);
        assertNotEquals(taskManager.getEpic(epicId), oldEpic, "Epics are equal");
        assertEquals(taskManager.getEpic(epicId), newEpic, "Epics are not equal");
    }

    @Test
    void updateSubtask() {
        final int subtaskId = subtask.getId();
        final Subtask oldSubtask = taskManager.getSubtask(subtaskId);
        final Subtask newSubtask = new Subtask("subtask1", "subtaskDescription", Status.IN_PROGRESS,
                subtaskId, 1, 20, LocalDateTime.of(2017, 1, 1, 1, 1));
        taskManager.updateSubtask(newSubtask);
        assertNotEquals(taskManager.getSubtask(subtaskId), oldSubtask, "Subtasks are equal");
        assertEquals(taskManager.getSubtask(subtaskId), newSubtask, "Subtasks are not equal");
        assertEquals(taskManager.getEpic(newSubtask.getEpicId()).getStatus(), Status.IN_PROGRESS, "Incorrect epic status");
    }

    @Test
    void deleteTask() {
        final int taskId = task.getId();
        taskManager.getTask(taskId);
        taskManager.deleteTask(taskId);
        assertEquals(0, taskManager.getTasks().size(), "Incorrect task list size");
        assertEquals(0, taskManager.getHistory().size(), "Incorrect history list size");
    }

    @Test
    void deleteEpic() {
        final int epicId = epic.getId();
        taskManager.getEpic(epicId);
        taskManager.deleteEpic(epicId);
        assertEquals(0, taskManager.getEpics().size(), "Incorrect epic list size");
        assertEquals(0, taskManager.getSubtasks().size(), "Incorrect subtask list size");
        assertEquals(0, taskManager.getHistory().size(), "Incorrect history list size");
    }

    @Test
    void deleteSubtask() {
        final int epicId = epic.getId();
        final int subtaskId = subtask.getId();
        taskManager.getSubtask(subtaskId);
        taskManager.deleteSubtask(subtaskId);
        assertEquals(0, taskManager.getSubtasks().size(), "Incorrect subtask list size");
        assertEquals(0, taskManager.getHistory().size(), "Incorrect history list size");
        assertEquals(0, taskManager.getEpic(epicId).getSubtasks().size(), "Incorrect subtask list size in epic");
    }

    @Test
    void updateEpicStatus() {
        assertEquals(epic.getStatus(), Status.NEW, "Statuses are not equals");
        deleteSubtasks();
        taskManager.addNewSubtask(new Subtask("subtask1", "subtaskDescription", Status.DONE, 2,
                1, 15, LocalDateTime.of(2005, 1, 1, 1, 1)));
        assertEquals(epic.getStatus(), Status.DONE, "Statuses are not equals");
        taskManager.addNewSubtask(new Subtask("subtask1", "subtaskDescription", Status.NEW, 3,
                1, 15, LocalDateTime.of(2026, 1, 1, 1, 1)));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Statuses are not equals");
        taskManager.deleteSubtasks();
        assertEquals(epic.getStatus(), Status.NEW, "Statuses are not equals");
        taskManager.addNewSubtask(new Subtask("subtask1", "subtaskDescription", Status.IN_PROGRESS, 2,
                1, 15, LocalDateTime.of(2028, 1, 1, 1, 1)));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Statuses are not equals");
    }

    @Test
    void getPrioritizedTasks() {
        List<Task> list = taskManager.getPrioritizedTasks();
        assertEquals(list.toArray()[0], task, "Incorrect task position in prioritizedTasks");
        assertEquals(list.toArray()[1], subtask, "Incorrect task position in prioritizedTasks");
    }
}
