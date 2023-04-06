package ru.yandex.potapov.schedule;

import ru.yandex.potapov.schedule.manager.FileBackedTasksManager;
import ru.yandex.potapov.schedule.manager.Managers;
import ru.yandex.potapov.schedule.manager.TaskManager;
import ru.yandex.potapov.schedule.task.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/*
Несколько вопросов:
1. В классе InMemoryHistoryManager в 29 строке необходимо добавить node.prev = last, иначе prev будет null
    Но ведь в 28 строке мы передаем значение в конструктор. Почему так?
2. В задании сказано, что в FileBackedTasksManager для записи в файл необходимо переопределить toString() или создать свой метод
    Я пошел по 2 пути, потому что совсем не понял как в этой ситуации можно переопределить стандартный. Можно какой-нибудь пример, пожалуйста?
3. Почему мы создаём своё исключение и пробрасываем его, а не просто в catch пишем System.out.println("Ошибка")?
4. Не разобрался как работать с main в FileBackedTasksManager, поэтому проверял всё здесь через комментирование
 */

public class Main {

    public static void main(String[] args) {
        File file = Path.of("saveFile.csv").toFile();
        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubtasks());
        System.out.println(fileBackedTasksManager.getHistory());
    }
   /* public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", Status.NEW, 0));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>()));
        taskManager.addNewSubtask(new Subtask("1 подзача", "Описание 1 подзадачи", Status.NEW, 0, 2));
        taskManager.addNewSubtask(new Subtask("2 подзача", "Описание 2 подзадачи", Status.NEW, 0, 2));
        taskManager.addNewSubtask(new Subtask("3 подзача", "Описание 3 подзадачи", Status.NEW, 0, 2));
        taskManager.addNewEpic(new Epic("2 эпик", "Описание 2 эпика", Status.NEW, 0, new ArrayList<>()));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getSubtask(3));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getHistory());
    }*/
}
