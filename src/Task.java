public class Task {
    Object taskName;
    Object taskInformation;
    String taskStatus;

    public Task(Object taskName, Object taskInformation, String taskStatus) {
        this.taskName = taskName;
        this.taskInformation = taskInformation;
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus(String status) {
        return taskStatus = status;
    }
}
