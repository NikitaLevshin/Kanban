package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
