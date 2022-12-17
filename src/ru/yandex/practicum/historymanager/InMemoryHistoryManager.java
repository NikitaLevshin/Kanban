package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Integer> tasks = new ArrayList<>();
    private HashMap<Integer, TaskNode> tasksMap = new LinkedHashMap<>();
    private TaskNode<Task> historyHead;
    private TaskNode<Task> historyTail;

    public InMemoryHistoryManager() {
        historyHead = null;
        historyTail = null;
    }

    @Override
    public List<Integer> getHistory() {
        getTasks();
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (tasks.size() >= 10) {
            tasks.remove(0);
        }
        if (tasksMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        tasksMap.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove (int taskId) {
        if (tasksMap.containsKey(taskId)) {
            removeNode(tasksMap.get(taskId));
        }
    }

    public TaskNode<Task> linkLast(Task task) {
        TaskNode<Task> lastTask = new TaskNode<>(historyTail, task, null);
        if (historyTail == null) {
            historyTail = lastTask;
            historyHead = lastTask;
        } else {
            historyTail = lastTask;
            historyTail.getPrev().setNext(lastTask);
        }
        return lastTask;
    }
    public void getTasks() {
        tasks.clear();
        for (int id: tasksMap.keySet()) {
            tasks.add(id);
        }
    }
    public void removeNode(TaskNode<Task> Node) {
        if (Node == historyHead) {
            Node.getNext().setPrev(null);
            historyHead = Node.getNext();
        } else if (Node == historyTail) {
            Node.getPrev().setNext(null);
            historyTail = Node.getPrev();
        } else {
            Node.getPrev().setNext(Node.getNext());
            Node.getNext().setPrev(Node.getPrev());
        }
        tasksMap.remove(Node.getData().getId());
    }
}
