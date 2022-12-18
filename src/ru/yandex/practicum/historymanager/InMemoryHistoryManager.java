package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskNode;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, TaskNode> tasksMap = new LinkedHashMap<>();
    private TaskNode<Task> historyHead;
    private TaskNode<Task> historyTail;
    private int historySize = 0;

    public InMemoryHistoryManager() {
        historyHead = null;
        historyTail = null;
    }

    @Override
    public List<Integer> getHistory() {
        List<Integer> tasks = new ArrayList<>();
        TaskNode<Task> task = historyHead;
        for (int i = 0; i < historySize; i++) {
            tasks.add(task.data.getId());
            task = task.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        tasksMap.put(task.getId(), linkLast(task));
        historySize++;
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
    public void removeNode(TaskNode<Task> node) {
        if (node == historyHead) {
            node.getNext().setPrev(null);
            historyHead = node.getNext();
        } else if (node == historyTail) {
            node.getPrev().setNext(null);
            historyTail = node.getPrev();
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
        tasksMap.remove(node.getData().getId());
        historySize--;
    }
}
