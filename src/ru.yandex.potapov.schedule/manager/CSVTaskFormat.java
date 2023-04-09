package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String taskToString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + (task.getType().equals(TaskType.SUBTASK) ? task.getEpicId() : "");
    }

    public static Task fromString(String value) {
        Task task;
        if (value != null) {
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

    public static String historyToString(HistoryManager historyManager) {
        final List<Task> history = historyManager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        final String[] values = value.split(",");
        final ArrayList<Integer> ids = new ArrayList<>(values.length);
        for (String id : values) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
