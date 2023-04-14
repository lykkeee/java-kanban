package ru.yandex.potapov.schedule;

import ru.yandex.potapov.schedule.manager.FileBackedTasksManager;
import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;
import ru.yandex.potapov.schedule.task.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2020, 2, 1 ,1,1 )));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", Status.NEW, 0, 10, LocalDateTime.of(2020, 1, 1 ,1,1 )));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 2, 6, LocalDateTime.of(2020, 3, 1 ,1,1 )));
        taskManager.addNewSubtask(new Subtask("2 подзадача", "Описание 2 подзадачи", Status.NEW, 0, 2, 7, LocalDateTime.of(2020, 4, 1 ,1,1 )));
        taskManager.addNewSubtask(new Subtask("3 подзадача", "Описание 3 подзадачи", Status.NEW, 0, 2, 8, LocalDateTime.of(2020, 5, 1 ,1,1 )));
        taskManager.addNewEpic(new Epic("2 эпик", "Описание 2 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getEpic(2);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("------------------------------------------------------------------");
        File save = new File("resources/saveFile.csv");
        final TaskManager historyTaskManager = FileBackedTasksManager.loadFromFile(save);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(historyTaskManager.getHistory());
        System.out.println("------------------------------------------------------------------");
        System.out.println(taskManager.getPrioritizedTasks());
        taskManager.deleteSubtasks();
        System.out.println(taskManager.getPrioritizedTasks());
    }
}
