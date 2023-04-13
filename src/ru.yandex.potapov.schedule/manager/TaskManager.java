package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.Epic;
import ru.yandex.potapov.schedule.task.Subtask;
import ru.yandex.potapov.schedule.task.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    Subtask getSubtaskByEpic(int id, int epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void updateEpicStatus(int epicId);

    void calculateEpicTime(int epicId);

    Set<Task> getPrioritizedTasks();

    boolean checkTimeIntersection(Task task);

    List<Task> getHistory();
}
