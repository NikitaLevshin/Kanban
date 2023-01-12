package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

import java.io.File;

public class Managers {
    final static File TASK_FILE = new File("resources/tasks.csv");

    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile(TASK_FILE);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
