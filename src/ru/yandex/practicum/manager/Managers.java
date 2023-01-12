package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

import java.io.File;

import static ru.yandex.practicum.manager.FileBackedTaskManager.file;

public class Managers {

    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
