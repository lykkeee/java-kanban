import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId() {
        this.id++;
    }

    ArrayList<HashMap<Integer, Task>> tasksList = new ArrayList<>();
    ArrayList<HashMap<Integer, Epic>> epicList = new ArrayList<>();


    public void createNewTask(Object taskName, Object taskInformation, String taskStatus) {
        Task task = new Task(taskName, taskInformation, taskStatus);
        HashMap<Integer, Task> taskHashMap = new HashMap<>();
        taskHashMap.put(id, task);
        tasksList.add(taskHashMap);
        setId();
    }

    public void createNewEpic(Object taskName, Object taskInformation, String taskStatus) {
        Epic epic = new Epic(taskName, taskInformation, taskStatus);
        HashMap<Integer, Epic> epicHashMap = new HashMap<>();
        epicHashMap.put(id, epic);
        epicList.add(epicHashMap);
        setId();
    }


    public void showAllTasks() {
        for (HashMap<Integer, Task> integerTaskHashMap : tasksList) {
            for (Integer key : integerTaskHashMap.keySet()) {
                System.out.println(integerTaskHashMap.get(key).taskName);
            }
        }
    }
    public void showAllEpics() {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (Integer key : epicHashMap.keySet()) {
                System.out.println(epicHashMap.get(key).taskName);
            }
        }
    }
    public void showAllSubtasks() {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (Integer key : epicHashMap.keySet()) {
                for(HashMap<Integer, Subtask> subtaskHashMap : epicHashMap.get(key).subtaskList) {
                    for(Integer key_2 : subtaskHashMap.keySet()) {
                        System.out.println(subtaskHashMap.get(key_2).taskName);
                    }
                }
            }
        }
    }
    public void showAllSubtasksByEpic(Integer id) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (Integer key : epicHashMap.keySet()) {
                if(id.equals(key)) {
                    for (HashMap<Integer, Subtask> subtaskHashMap : epicHashMap.get(key).subtaskList) {
                        for (Integer key_2 : subtaskHashMap.keySet()) {
                            System.out.println(subtaskHashMap.get(key_2).taskName);
                        }
                    }
                    break;
                }
            }
        }
    }

    public void deleteAllTasks() {
        for (HashMap<Integer, Task> integerTaskHashMap : tasksList) {
            integerTaskHashMap.clear();
        }
    }
    public void deleteAllEpics() {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            epicHashMap.clear();
        }
    }
    public void deleteAllSubtasks() {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for(Integer key : epicHashMap.keySet()) {
                for (HashMap<Integer, Subtask> subtaskHashMap : epicHashMap.get(key).subtaskList) {
                    subtaskHashMap.clear();
                }
            }
        }
    }

    public void showTask(Integer id) {
        for (HashMap<Integer, Task> taskHashMap : tasksList) {
            for (int key : taskHashMap.keySet()) {
                if (id.equals(key)) {
                    System.out.println(taskHashMap.get(key).taskName);
                    break;
                }
            }
        }
    }

    public void showEpic(Integer id) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (int key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    System.out.println(epicHashMap.get(key).taskName);
                    break;
                }
            }
        }
    }

    public void showSubtask(Integer id, Integer id_2) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (int key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    for (HashMap<Integer, Subtask> subtaskHashMap : epicHashMap.get(key).subtaskList) {
                        for (int key_2 : subtaskHashMap.keySet()) {
                            if (id_2.equals(key_2)) {
                                System.out.println(subtaskHashMap.get(key_2).taskName);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void refreshTask(Integer id, Object taskName, Object taskInformation, String taskStatus) {
        for (HashMap<Integer, Task> taskHashMap : tasksList) {
            for (int key : taskHashMap.keySet()) {
                if (id.equals(key)) {
                    Task task = new Task(taskName, taskInformation, taskStatus);
                    taskHashMap.put(key, task);
                    break;
                }
            }
        }
    }

    public void refreshEpic(Integer id, Object taskName, Object taskInformation, String taskStatus) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (int key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    Epic epic = new Epic(taskName, taskInformation, taskStatus);
                    epicHashMap.put(key, epic);
                    break;
                }
            }
        }
    }

    public void refreshSubtask(Integer id, Integer id_2, Object taskName, Object taskInformation, String taskStatus) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (int key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    for (HashMap<Integer, Subtask> subtaskHashMap : epicHashMap.get(key).subtaskList) {
                        for (int key_2 : epicHashMap.keySet()) {
                            if (id_2.equals(key_2)) {
                                Subtask subtask = new Subtask(taskName, taskInformation, taskStatus);
                                subtaskHashMap.put(key, subtask);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    public void deleteTask(Integer id) {
        for (HashMap<Integer, Task> taskHashMap : tasksList) {
            for (Integer key : taskHashMap.keySet()) {
                if (id.equals(key)) {
                    taskHashMap.clear();
                }
            }
        }
    }

    public void deleteEpic(Integer id) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (Integer key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    epicHashMap.clear();
                }
            }
        }
    }

    public void deleteSubtask(Integer id, Integer id_2) {
        for (HashMap<Integer, Epic> epicHashMap : epicList) {
            for (int key : epicHashMap.keySet()) {
                if (id.equals(key)) {
                    for (HashMap<Integer, Subtask> SubtaskHashMap : epicHashMap.get(key).subtaskList) {
                        for (Integer key_2 : SubtaskHashMap.keySet()) {
                            if (id_2.equals(key_2)) {
                                SubtaskHashMap.clear();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


}