package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(file.getPath()), StandardOpenOption.TRUNCATE_EXISTING)) {
            bufferedWriter.write("id,type,name,status,description,startTime,duration,epic\n");
            if (!taskMap.isEmpty()) {
                for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toCsv());
                    bufferedWriter.write("\n");
                }

            }
            if (!epicMap.isEmpty()) {
                for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toCsv());
                    bufferedWriter.write("\n");
                }

            }
            if (!subTaskMap.isEmpty()) {
                for (Map.Entry<Integer, SubTask> entry : subTaskMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toCsv());
                    bufferedWriter.write("\n");
                }

            }
            if (!historyManager.getHistory().isEmpty()) {
                bufferedWriter.write("\n");
                bufferedWriter.write(historyToString(historyManager));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while (br.ready()) {
                    String line = br.readLine();
                    if (line.startsWith("id")) {
                        continue;
                    }
                    if (line.isEmpty()) {
                        List<Integer> historyIds = historyFromString(br.readLine());
                        for (Integer id : historyIds) {
                            if (taskManager.taskMap.containsKey(id)) taskManager.historyManager.add(taskManager.getTaskById(id));
                            else if (taskManager.epicMap.containsKey(id)) taskManager.historyManager.add(taskManager.getEpicById(id));
                            else if (taskManager.subTaskMap.containsKey(id)) taskManager.historyManager.add(taskManager.getSubTaskById(id));
                        }
                    } else {
                        Task task = taskFromString(line);
                        if (task != null) {
                            switch (task.getType()) {
                                case TASK:
                                    taskManager.newTask(task);
                                    break;
                                case SUBTASK:
                                    taskManager.newSubTask((SubTask) task);
                                    break;
                                case EPIC:
                                    taskManager.newEpic((Epic) task);
                                    break;
                                default:
                                    throw new IllegalArgumentException("Считано не задание");
                            }
                        }
                    }
                }
                return taskManager;
            } catch (IOException e) {
                throw new ManagerSaveException(e.getMessage());
            }
        }
        return taskManager;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasksHistory = manager.getHistory();
        StringJoiner sj = new StringJoiner(",");
        for (Task oneTask : tasksHistory) {
            Integer id = oneTask.getId();
            sj.add(id.toString());
        }
        return sj.toString();
    }

    public static Task taskFromString(String value) {
        String[] values = value.split(",");

        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = null;
        Long duration = null;
        if (!values[5].equals("null") && !values[6].equals("null")) {
            startTime = LocalDateTime.parse(values[5]);
            duration = Long.parseLong(values[6]);
        }
        switch (type) {
            case TASK:
                if (values[6].equals("null")) return new Task(id, name, status, description);
                else return new Task(id, name, status, description, startTime, duration);


            case EPIC:
                return new Epic(id, name, description);

            case SUBTASK:
                    int epicId = Integer.parseInt(values[7]);
                    if (values[6].equals("null")) return new SubTask(id, name, status, description, epicId);
                    return new SubTask(id, name, status, description, startTime, duration, epicId);
            default:
                throw new IllegalArgumentException("Такого типа заданий не существует");
        }
    }

    public static List <Integer> historyFromString(String value) {
        String[] history = value.split(",");
        List<Integer> historyIds = new ArrayList<>();

        for (String id : history) {
            historyIds.add(Integer.parseInt(id));
        }
        return historyIds;
    }

    @Override
    public int newTask(Task task) {
        super.newTask(task);
        save();
        return task.getId();
    }

    @Override
    public int newSubTask(SubTask subTask) {
        super.newSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public int newEpic(Epic epic) {
        super.newEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void epicTime(Epic epic) {
        super.epicTime(epic);
        save();
    }

    @Override
    public void epicDuration(Epic epic) {
        super.epicDuration(epic);
        save();
    }


    public static void main(String[] args) {
        FileBackedTaskManager taskManager = loadFromFile(new File("resources/tasks.csv"));
        System.out.println(taskManager.historyManager.getHistory());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());

        taskManager.getTaskById(1);
        System.out.println(taskManager.historyManager.getHistory());
    }
}
