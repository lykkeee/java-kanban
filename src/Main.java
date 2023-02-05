import java.util.ArrayList;

public class Main {                 // Не оформил коммит как в теории потому что очень много исправлений и я не понимаю как это правильно сделать
                                    // С вашими комментариями вроде разобрался. Они дают больше понимания, чем теория
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", "NEW", 0));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", "NEW", 0));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", "NEW", 0, new ArrayList<>()));
        taskManager.addNewSubtask(new Subtask("1 подзача", "Описание 1 подзадачи", "NEW", 0, 2));
        taskManager.addNewSubtask(new Subtask("2 подзача", "Описание 2 подзадачи", "NEW", 0, 2));
        taskManager.addNewEpic(new Epic("2 эпик", "Описание 2 эпика", "NEW", 0, new ArrayList<>()));
        taskManager.addNewSubtask(new Subtask("3 подзача", "Описание 3 подзадачи", "NEW", 0, 5));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.updateTask(new Task("1 задача", "Описание 1", "DONE", 0));
        taskManager.updateEpic(new Epic("1 эпик", "Описание 1 эпика", "DONE", 2, new ArrayList<>()));
        taskManager.updateSubtask(new Subtask("3 подзача", "Описание 3 подзадачи", "IN_PROGRESS", 6, 5));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.deleteTask(0);
        taskManager.deleteEpic(5);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }
}
