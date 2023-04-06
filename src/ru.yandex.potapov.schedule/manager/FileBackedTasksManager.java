package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private static File saveFile = new File("saveFile.csv");

    public FileBackedTasksManager(File file) {
        file = saveFile;
    }

    public void main(String[] args) {
        TaskManager manager = FileBackedTasksManager.loadFromFile(saveFile);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getHistory());
    }

    public static FileBackedTasksManager loadFromFile (File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;

            while (br.ready()) {
                line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    manager.subtasks.put(task.getId(), (Subtask) task);
                } else if (task instanceof Task) {
                    manager.tasks.put(task.getId(), task);
                }
            }
            Map<Integer, Task> tasksMap = new HashMap<>();
            tasksMap.putAll(manager.epics);
            tasksMap.putAll(manager.tasks);
            tasksMap.putAll(manager.subtasks);
            line = br.readLine();
            br.close();
            for (Integer id : historyFromString(line)) {
                Managers.getDefaultHistory().add(tasksMap.get(id));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении");
        }
        return manager;
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

    public void save() {
        try {
            Writer fileWriter = new FileWriter(saveFile);
            fileWriter.write("id,type,name,status,description,epic\n");
            Map<Integer, Task> tasksMap = new HashMap<>();
            tasksMap.putAll(tasks);
            tasksMap.putAll(subtasks);
            tasksMap.putAll(epics);

            for (Map.Entry<Integer, Task> entry : tasksMap.entrySet()) {
                fileWriter.write(taskToString(entry.getValue()) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
            fileWriter.close();

        } catch (IOException e) {
            throw new  ManagerSaveException("Ошибка при записи");
        }

    }

    public String taskToString(Task task) {
        TaskType taskType = null;
        String epicId = "";

        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            taskType = TaskType.SUBTASK;
            Integer id = ((Subtask) task).getEpicId();
            epicId = id.toString();
        } else if (task instanceof Task) {
            taskType = TaskType.TASK;
        }
        String str = task.getId() + "," + taskType + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + epicId;
        return str;
    }

    public static Task fromString(String value) {
        String content = value;
        Task task;
        if (content != null) {
                String[] parts = value.split(",");
                if (parts[1].equals(TaskType.TASK.toString())) {
                    task = new Task(parts[2], parts[4], Status.valueOf(parts[3]), parseInt(parts[0]));
                    return task;
                } else if (parts[1].equals(TaskType.EPIC.toString())) {
                    task = new Epic(parts[2], parts[4], Status.valueOf(parts[3]), parseInt(parts[0]), new ArrayList<>());
                    return task;
                } else if (parts[1].equals(TaskType.SUBTASK.toString())) {
                    task = new Subtask(parts[2], parts[4], Status.valueOf(parts[3]), parseInt(parts[0]), parseInt(parts[5]));
                    return task;
                }
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        List<String> history = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            Integer id = task.getId();
            history.add(id.toString());
        }
        return String.join(",", history);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] parts = null;
        if (value != null) {
            parts = value.split(",");
        }
        for (String ch : parts) {
            history.add(parseInt(ch));
        }
        return history;
    }
}
