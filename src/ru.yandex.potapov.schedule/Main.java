package ru.yandex.potapov.schedule;

import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;
import ru.yandex.potapov.schedule.task.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new Managers().getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", Status.NEW, 0));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>()));
        taskManager.addNewSubtask(new Subtask("1 подзача", "Описание 1 подзадачи", Status.NEW, 0, 2));
        taskManager.addNewSubtask(new Subtask("2 подзача", "Описание 2 подзадачи", Status.NEW, 0, 2));
        taskManager.addNewEpic(new Epic("2 эпик", "Описание 2 эпика", Status.NEW, 0, new ArrayList<>()));
        taskManager.addNewSubtask(new Subtask("3 подзача", "Описание 3 подзадачи", Status.NEW, 0, 5));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.updateTask(new Task("1 задача", "Описание 1", Status.DONE, 0));
        taskManager.updateEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 2, new ArrayList<>()));
        taskManager.updateSubtask(new Subtask("3 подзача", "Описание 3 подзадачи", Status.DONE, 6, 5));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.deleteTask(0);
        taskManager.deleteEpic(5);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getSubtask(3));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getHistory());
    }
}