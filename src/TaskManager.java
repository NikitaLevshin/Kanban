import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected int generateId;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();


    void newTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    void newSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        for (Epic oneEpic : epicMap.values()) {
            addSubTaskToEpic(oneEpic, subTask);
        }
    }

     void newEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    void addSubTaskToEpic(Epic epic, SubTask subTask) {
        if (epic.getId() == subTask.getEpicId()) {
            epic.addSubTask(subTask);
        }
    }

    //генерируем id для новых заданий
    int newId() {
        return ++generateId;
    }

    //получаем задачи по её идентификатору
    Task getTaskById(int id) {
        return taskMap.get(id);
    }

    SubTask getSubTaskById(int id) {
        for (SubTask oneSubTask : subTaskMap.values()) {
            if (oneSubTask.getId() == id) {
                return oneSubTask;
            }
        } return null;
    }

    Epic getEpicById(int id) {
        for (Epic oneEpic : epicMap.values()) {
            if (oneEpic.getId() == id) {
                return oneEpic;
            }
        } return null;
    }

    //получить список всех задач
    ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        if (!taskMap.isEmpty()) {
            for (Integer key : taskMap.keySet()) {
                result.add(taskMap.get(key));
            }
        }
        return result;
    }

    ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>();
        if (!subTaskMap.isEmpty()) {
            for (Integer key : subTaskMap.keySet()) {
                result.add(subTaskMap.get(key));
            }
        }
        return result;
    }

    ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> result = new ArrayList<>();
        if (!epicMap.isEmpty()) {
            for (Integer key : epicMap.keySet()) {
                result.add(epicMap.get(key));
            }
        }
        return result;
    }

    ArrayList<SubTask> getAllSubTasksInEpic(Epic epic) {
        return epic.getSubTasks();
    }

    //удаляем задачи по идентификатору
    void removeTaskById(int id) {
        taskMap.remove(id);
    }

    void removeSubTaskById(int id) {
        subTaskMap.remove(id);
    }

    void removeEpicById(int id) {
        epicMap.remove(id);
    }

    //удаляем все задачи
    void removeTasks() {
        taskMap.clear();
    }

    void removeSubTasks() {
        subTaskMap.clear();
    }

    void removeEpics() {
        epicMap.clear();
    }

    //обновляем задачи
    void upgradeTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    void upgradeSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
    }

    void upgradeEpic(Epic epic) {
        epic.setStatus(epic.statusKey());
        epicMap.put(epic.getId(), epic);
    }
}
