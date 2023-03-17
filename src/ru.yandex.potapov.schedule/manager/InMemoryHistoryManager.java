package ru.yandex.potapov.schedule.manager;

import ru.yandex.potapov.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager    {

    class Node {
        public Task task;
        public Node prev;
        public Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }

    public final HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;


    @Override
    public void add(Task task) {
        final Node oldLast = last;
        final Node newNode = new Node(task, last, null);
        newNode.prev = last;
        if (oldLast != null) {
            oldLast.next = newNode;

        } else {
            first = newNode;
        }
        last = newNode;
        remove(task.getId());
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        final List<Task> history = new ArrayList<>();
        Node node = first;
        if (first == null) {
            return history;
        } else {
            while (node != null) {
                if(history.contains(node.task)){
                    history.remove(node.task);
                }
                history.add(node.task);
                node = node.next;
            }
        }
        return history;
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node node = history.get(id);
            if (node.prev != null && node.next != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else if (node.prev != null) {
                node.prev.next = node.next;
            } else if (node.next != null) {
                node.next.prev = node.prev;
            };
            if (first == node) {
                first = node.next;
            } else if (last == node) {
                last = node.prev;
            }
            history.remove(id);
        }
    }
}
