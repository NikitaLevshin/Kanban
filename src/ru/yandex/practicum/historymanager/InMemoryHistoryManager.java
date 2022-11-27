package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Integer> tasks = new ArrayList<>();

    @Override
    public List<Integer> getHistory() {
        return tasks;
    }

    @Override
    public void add(int taskId) {
        if(tasks.size() < 10) {
            tasks.add(taskId);
        } else {
            tasks.remove(0);
            tasks.add(taskId);
        }
    }
}
