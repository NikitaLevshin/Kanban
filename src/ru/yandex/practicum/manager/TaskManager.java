package ru.yandex.practicum.manager;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

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
            updateEpicStatus(epic);
        }
    }

    //генерируем id для новых заданий
    private int newId() {
        return ++generateId;
    }

    //получаем задачи по её идентификатору
    public Task getTaskById(int id) {
        if (taskMap.get(id) != null) {
            return taskMap.get(id);
        } return null;
    }

    public SubTask getSubTaskById(int id) {
        if (subTaskMap.get(id) != null) {
            return subTaskMap.get(id);
        } return null;
    }

    public Epic getEpicById(int id) {
        if (epicMap.get(id) != null) {
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
        ArrayList<Integer> ids = new ArrayList<>(epic.getSubTasks());
        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer id : ids) {
            result.add(subTaskMap.get(id));
        }
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
        updateEpicStatus(oneEpic);
    }

    public void removeEpicById(int id) {
        Epic oneEpic = epicMap.get(id);
        for (Integer ids : oneEpic.getSubTasks()) {
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
            updateEpicStatus(oneEpic);
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
        Epic oneEpic = epicMap.get(subTask.getEpicId());
        updateEpicStatus(oneEpic);
    }

    public void updateEpic(Epic epic) {
        updateEpicStatus(epic);
        epicMap.put(epic.getId(), epic);
    }

    public void updateEpicStatus(Epic epic) {
        int countNew = 0;
        int countDone = 0;

        for (Integer id : epic.getSubTasks()) {
            SubTask oneSubTask = subTaskMap.get(id);
            if (oneSubTask != null) {
                switch (oneSubTask.getStatus()) {
                    case NEW:
                        countNew++;
                        break;
                    case DONE:
                        countDone++;
                        break;
                    }
                }
            }

        if (countNew == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDone == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
