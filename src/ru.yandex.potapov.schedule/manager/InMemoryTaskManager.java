package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.*;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected static final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int addNewTask(Task task) {
        add(task);
        int id = generatorId();
        tasks.put(id, task);
        task.setId(id);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = generatorId();
        epics.put(id, epic);
        epic.setId(id);
        updateEpicById(id);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        add(subtask);
        int id = generatorId();
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(id);
        subtask.setId(id);
        updateEpicById(epicId);
        return id;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    @Override
    public void deleteTasks() {
        for(Task task : getPrioritizedTasks()){
            for(Task t : tasks.values()){
                if (task.equals(t)) {
                    prioritizedTasks.remove(task);
                }
            }
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for(Task task : getPrioritizedTasks()){
            for(Subtask sub : subtasks.values()){
                if (task.equals(sub)) {
                    prioritizedTasks.remove(task);
                }
            }
        }
        for (Epic epic : epics.values()) {
            epic.cleanSubtasksId();
            updateEpicById(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Subtask getSubtaskByEpic(int id, int epicId) {
        Subtask subtask = null;
        if (epicId == subtasks.get(id).getEpicId()) {
            subtask = subtasks.get(id);
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        prioritizedTasks.remove(tasks.get(id));
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        add(task);
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasks(savedEpic.getSubtasks());
        epics.put(id, epic);
        updateEpicById(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        prioritizedTasks.remove(subtasks.get(id));
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        add(subtask);
        updateEpicById(subtask.getEpicId());
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtasks()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        prioritizedTasks.remove(subtasks.get(id));
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        subtasks.remove(id);
        epic.deleteSubtaskId(id);
        historyManager.remove(id);
        updateEpicById(epic.getId());
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subs = epic.getSubtasks();
        if (subs.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        Status status = null;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status == subtask.getStatus()
                    && status != Status.IN_PROGRESS) {
                continue;
            }
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicDuration(Epic epic) {
        List<Integer> subs = epic.getSubtasks();
        if (subs.isEmpty()) {
            epic.setDuration(0L);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        long duration = 0L;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            final LocalDateTime startTime = subtask.getStartTime();
            final LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
            duration += subtask.getDuration();
        }
        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
    }

    protected void updateEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        updateEpicStatus(epicId);
        updateEpicDuration(epic);
    }

    private void add(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();
        for (Task t : prioritizedTasks) {
            final LocalDateTime existStart = t.getStartTime();
            final LocalDateTime existEnd = t.getEndTime();
            if (!endTime.isAfter(existStart)) {
                continue;
            }
            if (!existEnd.isAfter(startTime)) {
                continue;
            }
            throw new TimeIntersectionException("Задача пересекаются с id=" + t.getId() + " c " + existStart + " по " + existEnd);
        }
        prioritizedTasks.add(task);
    }

    protected int generatorId() {
        return this.id++;
    }
}