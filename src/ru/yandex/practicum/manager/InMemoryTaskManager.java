package ru.yandex.practicum.manager;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class InMemoryTaskManager implements TaskManager {

    public final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int generateId;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
        Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));




    @Override
    public int newTask(Task task) {
        if (validator.test(task)) {
            if (task.getId() == null) task.setId(newId());
            task.updateEndTime();
            taskMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new DateTimeException("Время старта и конца заданий не должны пересекаться");
        }
        return task.getId();
    }

    @Override
    public int newSubTask(SubTask subTask) {
        if (validator.test(subTask)) {
            subTask.updateEndTime();
            if (subTask.getId() == null) subTask.setId(newId());
            subTaskMap.put(subTask.getId(), subTask);
            prioritizedTasks.add(subTask);
            addSubTaskToEpic(epicMap.get(subTask.getEpicId()), subTask);
        } else {
            throw new DateTimeException("Время старта и конца заданий не должны пересекаться");
        }
        return subTask.getId();
    }

    @Override
    public int newEpic(Epic epic) {
            if (epic.getId() == null) epic.setId(newId());
            epicMap.put(epic.getId(), epic);
        return epic.getId();
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        if (epic.getId() == subTask.getEpicId()) {
            epic.addSubTask(subTask);
            epicTime(epicMap.get(subTask.getEpicId()));
            epicDuration(epicMap.get(subTask.getEpicId()));
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
            historyManager.add(taskMap.get(id));
            return taskMap.get(id);
        } return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTaskMap.get(id) != null) {
            historyManager.add(subTaskMap.get(id));
            return subTaskMap.get(id);
        } return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicMap.get(id) != null) {
            historyManager.add(epicMap.get(id));
            getAllSubTasksInEpic(id);
            return epicMap.get(id);
        } return null;
    }


    //получить список всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksInEpic(int epicId) {
        ArrayList<SubTask> result = new ArrayList<>();
        Epic oneEpic = epicMap.get(epicId);
        for (Integer ids : oneEpic.getSubTasks()) {
            result.add(subTaskMap.get(ids));
            historyManager.add(subTaskMap.get(ids));
        }
        return result;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    //удаляем задачи по идентификатору
    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(taskMap.get(id));
        taskMap.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        Epic oneEpic = epicMap.get((subTaskMap.get(id).getEpicId()));
        oneEpic.removeOneSubTask(subTaskMap.get(id));
        historyManager.remove(id);
        prioritizedTasks.remove(subTaskMap.get(id));
        subTaskMap.remove(id);
        updateEpicStatus(oneEpic);
    }

    @Override
    public void removeEpicById(int id) {
        Epic oneEpic = epicMap.get(id);
        for (Integer ids : oneEpic.getSubTasks()) {
            historyManager.remove(ids);
            subTaskMap.remove(ids);
        }
        historyManager.remove(id);
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
        if (validator.test(task)) {
            task.updateEndTime();
            taskMap.put(task.getId(), task);
        } else {
            throw new DateTimeException("Задача пересекается с другой по времени");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (validator.test(subTask)) {
            subTask.updateEndTime();
            subTaskMap.put(subTask.getId(), subTask);
            Epic oneEpic = epicMap.get(subTask.getEpicId());
            updateEpicStatus(oneEpic);
            epicTime(epicMap.get(subTask.getEpicId()));
            epicDuration(epicMap.get(subTask.getEpicId()));
        } else {
            throw new DateTimeException("Задача пересекается с другой по времени");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        updateEpicStatus(epic);
        epicMap.put(epic.getId(), epic);
        epicTime(epic);
        epicDuration(epic);
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

    protected void epicTime(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        for (Integer id : epic.getSubTasks()) {
            SubTask subTask = subTaskMap.get(id);
            if (subTask.getStartTime() != null && startTime.isAfter(subTask.getStartTime())) {
                startTime = subTask.getStartTime();
            } if (subTask.getEndTime() != null && endTime.isBefore(subTask.getEndTime())) {
                endTime = subTask.getEndTime();
            } if (startTime != LocalDateTime.MAX) epic.setStartTime(startTime);
            if (endTime != LocalDateTime.MIN) epic.setEndTime(endTime);
        }
    }

    protected void epicDuration(Epic epic) {
        if (epic.getStartTime() != null && epic.getEndTime() != null) {
            epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()).toMinutes());
        }
    }

    private final Predicate<Task> validator = task -> {
        if (task.getStartTime() == null || task.getDuration() == null) return true;
        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();
        for (Task sortedTask : prioritizedTasks) {
            if (sortedTask.getStartTime() != null && sortedTask.getEndTime() != null) {
                LocalDateTime sortedTaskStart = sortedTask.getStartTime();
                LocalDateTime sortedTaskEnd = sortedTask.getEndTime();
                if (taskStartTime == sortedTaskStart || taskEndTime == sortedTaskEnd) return false;
                if (taskStartTime.isAfter(sortedTaskStart) && taskStartTime.isBefore(sortedTaskEnd)) return false;
                if (taskEndTime != null && (taskEndTime.isAfter(sortedTaskStart) && taskEndTime.isBefore(sortedTaskEnd))) return false;
            }
        }
        return true;
    };

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
