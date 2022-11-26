package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return tasks;
    }

    @Override
    public void add(Task task) {
        if(tasks.size() < 11) {
            tasks.add(task);
        } else {
            tasks.remove(0);
            tasks.add(task);
        }
    }
}
