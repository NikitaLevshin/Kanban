package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    public final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int generateId;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();



    @Override
    public int newTask(Task task) {
        task.setId(newId());
        taskMap.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int newSubTask(SubTask subTask) {
        subTask.setId(newId());
        subTaskMap.put(subTask.getId(), subTask);
        addSubTaskToEpic(epicMap.get(subTask.getEpicId()), subTask);
        return subTask.getId();
    }

    @Override
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

    @Override
    //получаем задачи по её идентификатору
    public Task getTaskById(int id) {
        if (taskMap.get(id) != null) {
            historyManager.add(taskMap.get(id).getId());
            return taskMap.get(id);
        } return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTaskMap.get(id) != null) {
            historyManager.add(subTaskMap.get(id).getId());
            return subTaskMap.get(id);
        } return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicMap.get(id) != null) {
            historyManager.add(epicMap.get(id).getId());
            return epicMap.get(id);
        } return null;
    }


    //получить список всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>(taskMap.values());
        return result;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>(subTaskMap.values());
        return result;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> result = new ArrayList<>(epicMap.values());
        return result;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksInEpic(Epic epic) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer ids : epic.getSubTasks()) {
            result.add(subTaskMap.get(ids));
        }
        return result;
    }

    //удаляем задачи по идентификатору
    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        Epic oneEpic = epicMap.get((subTaskMap.get(id).getEpicId()));
        oneEpic.removeOneSubTask(subTaskMap.get(id));
        subTaskMap.remove(id);
        updateEpicStatus(oneEpic);
    }

    @Override
    public void removeEpicById(int id) {
        Epic oneEpic = epicMap.get(id);
        for (Integer ids : oneEpic.getSubTasks()) {
            subTaskMap.remove(ids);
        }
        epicMap.remove(id);
    }

    //удаляем все задачи
    @Override
    public void removeTasks() {
        taskMap.clear();
    }

    @Override
    public void removeSubTasks() {
        subTaskMap.clear();
        for (Epic oneEpic : epicMap.values()) {
            oneEpic.removeSubTasks();
            updateEpicStatus(oneEpic);
        }
    }

    @Override
    public void removeEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    //обновляем задачи
    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        Epic oneEpic = epicMap.get(subTask.getEpicId());
        updateEpicStatus(oneEpic);
    }

    @Override
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

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
