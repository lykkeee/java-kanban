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
        status(epic);
        return id;
    }

    public int addNewSubtask(Subtask subtask) {
        int id = generatorId();
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(id);
        subtask.setId(id);
        status(epics.get(epicId));
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
        for (int key : tasks.keySet()) {
            tasks.remove(key);
        }
    }

    public void deleteEpics() {
        for (int key : epics.keySet()) {
            epics.remove(key);
        }
    }

    public void deleteSubtasks() {
        for (int key : subtasks.keySet()) {
            subtasks.remove(key);
        }
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
        status(epic);
        epics.put(id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        status(epics.get(subtask.getEpicId()));
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        for (Subtask subtask : subtaskArrayList) {
            if (subtask.getEpicId() == id) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    public boolean completeAllSubtasksByEpic(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        boolean isAllEpicCompleted = true;
        if (subtaskArrayList.size() != 0) {
            for (Subtask subtask : subtaskArrayList) {
                if (subtask.getEpicId() == epicId) {
                    if (!subtask.getStatus().equals("DONE")) {
                        isAllEpicCompleted = false;
                        break;
                    }
                } else {
                    isAllEpicCompleted = false;
                }
            }
        } else {
            isAllEpicCompleted = false;
        }
        return isAllEpicCompleted;
    }

    public boolean completeSubtaskByEpic(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        boolean isEpicCompleted = true;
        for (Subtask subtask : subtaskArrayList) {
            if (subtask.getEpicId() == epicId) {
                if (subtask.getStatus().equals("IN_PROGRESS")) {
                    isEpicCompleted = false;
                    break;
                }
            }
        }
        return isEpicCompleted;
    }

    public void status(Epic epic) {
        if (!completeSubtaskByEpic(epic.getId())) {
            epic.setStatus("IN_PROGRESS");
        } else if (completeAllSubtasksByEpic(epic.getId())) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("NEW");
        }
    }
}