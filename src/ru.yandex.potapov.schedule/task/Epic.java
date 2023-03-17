package ru.yandex.potapov.schedule.task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description, Status status, int id, ArrayList<Integer> subtasks) {
        super(name, description, status, id);
        this.subtasks = subtasks;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }
    public void cleanSubtasksId() {
        subtasks.clear();
    }
    public void deleteSubtaskId(int id) {
        int counter = 0;
        for (int subtask : subtasks) {
            if (subtask == id) {
                subtasks.remove(counter);
                break;
            }
            counter++;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                "} " + super.toString();
    }
}

