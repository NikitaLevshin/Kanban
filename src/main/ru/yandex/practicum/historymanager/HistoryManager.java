package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();
    void add(Task task);
    void remove (int taskId);
}
