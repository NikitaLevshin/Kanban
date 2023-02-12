package ru.yandex.practicum.manager;

import ru.yandex.practicum.api.HttpTaskManager;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

import java.io.File;

public class Managers {
    final static String URL = "http://localhost:8080/";

    public static TaskManager getDefault() {
        return new HttpTaskManager(URL);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
