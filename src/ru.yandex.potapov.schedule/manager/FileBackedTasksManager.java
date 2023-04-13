package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File saveFile;

    public FileBackedTasksManager(File file) {
        saveFile = file;
    }

    public void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("1 задача", "Описание 1", Status.NEW, 0, 15, LocalDateTime.now()));
        taskManager.addNewTask(new Task("2 задача", "Описание 2", Status.NEW, 0, 10, LocalDateTime.now()));
        taskManager.addNewEpic(new Epic("1 эпик", "Описание 1 эпика", Status.NEW, 0, new ArrayList<>(), 0,
                LocalDateTime.now(), LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("1 подзадача", "Описание 1 подзадачи", Status.NEW, 0, 2, 6, LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("2 подзадача", "Описание 2 подзадачи", Status.NEW, 0, 2, 7, LocalDateTime.now()));
        taskManager.addNewSubtask(new Subtask("3 подзадача", "Описание 3 подзадачи", Status.NEW, 0, 2, 8, LocalDateTime.now()));
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
        File save = new File("resources/saveFile.csv");
        final TaskManager historyTaskManager = FileBackedTasksManager.loadFromFile(save);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(historyTaskManager.getHistory());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.fromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.getSubtasks().add(subtask.getId());
            }
            for (Integer taskId : history) {
                taskManager.historyManager.add(taskManager.findTask(taskId));
            }
            taskManager.id = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }
        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epics.get(id);
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(CSVTaskFormat.getHeader());
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.taskToString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.taskToString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.taskToString(task));
                writer.newLine();
            }
            writer.newLine();
            writer.write(CSVTaskFormat.historyToString(historyManager));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + saveFile.getName(), e);
        }
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Subtask getSubtaskByEpic(int id, int epicId) {
        Subtask subtask = super.getSubtaskByEpic(id, epicId);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void calculateEpicTime(int epicId) {
        super.calculateEpicTime(epicId);
        save();
    }
}
