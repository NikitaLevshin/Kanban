package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    int newTask(Task task);

    int newSubTask(SubTask subTask);

    int newEpic(Epic epic);

    //получаем задачи по её идентификатору
    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    //получить список всех задач
    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasksInEpic(int epicId);

    //удаляем задачи по идентификатору
    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    //удаляем все задачи
    void removeTasks();

    void removeSubTasks();

    void removeEpics();

    //обновляем задачи
    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

}
