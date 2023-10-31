package ru.yandex.potapov.schedule;

import ru.yandex.potapov.schedule.manager.*;
import ru.yandex.potapov.schedule.server.HttpTaskServer;
import ru.yandex.potapov.schedule.server.KVServer;
import ru.yandex.potapov.schedule.task.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer server = new HttpTaskServer();
        TaskManager taskManager = Managers.getDefault();
        server.start();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.of(2015, 1, 1, 1, 1)));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", Status.NEW, 0, 10, LocalDateTime.of(2014, 1, 1, 1, 1)));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 2, 6, LocalDateTime.of(2013, 1, 1, 1, 1)));
        taskManager.addNewSubtask(new Subtask("2 подзадача", "Описание 2 подзадачи", Status.NEW, 0, 2, 7, LocalDateTime.of(2012, 1, 1, 1, 1)));
        taskManager.addNewSubtask(new Subtask("3 подзадача", "Описание 3 подзадачи", Status.NEW, 0, 2, 8, LocalDateTime.of(2011, 1, 1, 1, 1)));
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
        System.out.println("------------------------------------------------------------------");
        TaskManager manager = new HttpTaskManager("http://localhost:8078").loadFromServer();
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        manager.getTask(1);
        System.out.println(manager.getHistory());
        server.stop();
        kvServer.stop();

    }
}
