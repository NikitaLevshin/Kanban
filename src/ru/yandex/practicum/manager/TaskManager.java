package ru.yandex.practicum.manager;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected int generateId;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();


    public int newTask(Task task) {
        task.setId(newId());
        taskMap.put(task.getId(), task);
        return task.getId();
    }

    public int newSubTask(SubTask subTask) {
        subTask.setId(newId());
        subTaskMap.put(subTask.getId(), subTask);
        addSubTaskToEpic(epicMap.get(subTask.getEpicId()), subTask);
        return subTask.getId();
    }

     public int newEpic(Epic epic) {
        epic.setId(newId());
        epicMap.put(epic.getId(), epic);
        return epic.getId();
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        if (epic.getId() == subTask.getEpicId()) {
            epic.addSubTask(subTask);
        }
    }

    //генерируем id для новых заданий
    private int newId() {
        return ++generateId;
    }

    //получаем задачи по её идентификатору
    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        if (subTaskMap.get(id).getId() == id) {
            return subTaskMap.get(id);
        } return null;
    }

    public Epic getEpicById(int id) {
        if (epicMap.get(id).getId() == id) {
            return epicMap.get(id);
        } return null;
    }

    //получить список всех задач
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>(taskMap.values());
        return result;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>(subTaskMap.values());
        return result;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> result = new ArrayList<>(epicMap.values());
        return result;
    }

    public ArrayList<SubTask> getAllSubTasksInEpic(Epic epic) {
        ArrayList<SubTask> result = new ArrayList<>(epic.getSubTasks());
        return result;
    }

    //удаляем задачи по идентификатору
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    public void removeSubTaskById(int id) {
        Epic oneEpic = getEpicById(subTaskMap.get(id).getEpicId());
        oneEpic.removeOneSubTask(subTaskMap.get(id));
        subTaskMap.remove(id);
    }

    public void removeEpicById(int id) {
        Epic oneEpic = epicMap.get(id);
        for (Integer ids : oneEpic.getSubTasksId()) {
            subTaskMap.remove(ids);
        }
        epicMap.remove(id);
    }

    //удаляем все задачи
    public void removeTasks() {
        taskMap.clear();
    }

    public void removeSubTasks() {
        subTaskMap.clear();
        for (Epic oneEpic : epicMap.values()) {
            oneEpic.removeSubTasks();
        }
    }

    public void removeEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    //обновляем задачи
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        epicMap.get(subTask.getEpicId()).updateStatus();
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }
}
