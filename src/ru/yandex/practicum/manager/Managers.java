package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile(new File("resources/tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
