import java.util.ArrayList;
import java.util.HashMap;
public class Epic extends Task{
    Manager manager = new Manager();


    public Epic(Object taskName, Object taskInformation, String taskStatus) {
        super(taskName, taskInformation, taskStatus);
    }
    ArrayList<HashMap<Integer, Subtask>> subtaskList = new ArrayList<>();

    public void createNewSubtask(Object taskName, Object taskInformation, String taskStatus) {
        int id = manager.getId();
        Subtask subtask = new Subtask(taskName, taskInformation, taskStatus);
        HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
        subtaskHashMap.put(id, subtask);
        subtaskList.add(subtaskHashMap);
        manager.setId();
    }

    public boolean completeAllSubtask(){
        boolean isAllEpicCompleted = true;
        for (HashMap<Integer, Subtask> subtaskHashMap : subtaskList) {
            for (Subtask subtask : subtaskHashMap.values()){
                if (Status.DONE.equals(subtask.taskStatus)) {
                    isAllEpicCompleted = false;
                    break;
                }
            }
        }
        return isAllEpicCompleted;
    }

    public boolean completeSubtask(){
        boolean isEpicCompleted = true;
        for (HashMap<Integer, Subtask> subtaskHashMap : subtaskList) {
            for (Subtask subtask : subtaskHashMap.values()) {
                if (!Status.NEW.equals(subtask.taskStatus)) {
                    isEpicCompleted = false;
                    break;
                }
            }
        }
        return isEpicCompleted;
    }

    @Override
    public String getTaskStatus(String status) {
        for (HashMap<Integer, Subtask> subtaskHashMap : subtaskList) {
            if (subtaskHashMap.isEmpty()) {
                this.taskStatus = Status.NEW;
            } else if (completeSubtask()) {
                this.taskStatus = Status.NEW;
            } else if (completeAllSubtask()) {
                this.taskStatus = Status.DONE;
            } else {
                this.taskStatus = Status.IN_PROGRESS;
            }
        }
        return this.taskStatus;
    }
}

