package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskNode;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, TaskNode> tasksMap = new LinkedHashMap<>();
    CustomLinkedList linkedList = new CustomLinkedList();


    @Override
    public List<Task> getHistory() {
        return linkedList.getTasks();
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        tasksMap.put(task.getId(), linkedList.linkLast(task));
    }

    @Override
    public void remove (int taskId) {
        if (tasksMap.containsKey(taskId)) {
            linkedList.removeNode(tasksMap.get(taskId));
        }
    }



    private class CustomLinkedList {
        private TaskNode<Task> historyHead;
        private TaskNode<Task> historyTail;

        private CustomLinkedList() {
            historyHead = null;
            historyTail = null;
        }
        private TaskNode<Task> linkLast(Task task) {
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
        private void removeNode(TaskNode<Task> node) {
            if (historyHead == historyTail) {
                node.setNext(null);
                historyHead = null;
                node.setPrev(null);
                historyTail = null;
            } else if (node == historyHead) {
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
        }

        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            TaskNode<Task> task = historyHead;
            while (task != null) {
                tasks.add(task.data);
                task = task.next;
            }
            return tasks;
        }
    }
}
