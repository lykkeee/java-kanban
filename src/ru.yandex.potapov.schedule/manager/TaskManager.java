package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.*;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int generatorId() {
        return this.id++;
    }

    public int addNewTask(Task task) {
        int id = generatorId();
        tasks.put(id, task);
        task.setId(id);
        return id;
    }

    public int addNewEpic(Epic epic) {
        int id = generatorId();
        epics.put(id, epic);
        epic.setId(id);
        updateEpicStatus(epic.getId());
        return id;
    }

    public int addNewSubtask(Subtask subtask) {
        int id = generatorId();
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(id);
        subtask.setId(id);
        updateEpicStatus(epicId);
        return id;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }


    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtasksId();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Subtask getSubtaskByEpic(int id, int epicId) {
        Subtask subtask = null;
        if (epicId == subtasks.get(id).getEpicId()) {
            subtask = subtasks.get(id);
        }
        return subtask;
    }

    public void updateTask(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasks(savedEpic.getSubtasks());
        epics.put(id, epic);
        updateEpicStatus(epic.getId());
    }

    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasks()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtask(int id) {
        subtasks.remove(id);
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskId(id);
        updateEpicStatus(epic.getId());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subs = epic.getSubtasks();
        if (subs.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }
        String status = null;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status.equals(subtask.getStatus())
                    && !status.equals("IN_PROGRESS")) {
                continue;
            }
            epic.setStatus("IN_PROGRESS");
            return;
        }
        epic.setStatus(status);
    }
}