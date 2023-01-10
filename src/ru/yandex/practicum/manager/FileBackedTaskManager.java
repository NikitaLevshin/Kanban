package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final static String PATH = "resources/tasks.csv";

    public void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(PATH), StandardOpenOption.TRUNCATE_EXISTING)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            if (!taskMap.isEmpty()) {
                for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toString());
                    bufferedWriter.write("\n");
                }

            }
            if (!epicMap.isEmpty()) {
                for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toString());
                    bufferedWriter.write("\n");
                }

            }
            if (!subTaskMap.isEmpty()) {
                for (Map.Entry<Integer, SubTask> entry : subTaskMap.entrySet()) {
                    bufferedWriter.write(entry.getValue().toString());
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

    public FileBackedTaskManager loadFromFile(File file) {
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                FileBackedTaskManager taskManager = new FileBackedTaskManager();
                while (br.ready()) {
                    String line = br.readLine();
                    if (line.startsWith("id")) {
                    } else if (line.isEmpty()) {
                        List<Integer> historyIds = historyFromString(br.readLine());
                        for (Integer id : historyIds) {
                            if (taskMap.containsKey(id)) taskManager.historyManager.add(getTaskById(id));
                            else if (epicMap.containsKey(id)) taskManager.historyManager.add(getEpicById(id));
                            else if (subTaskMap.containsKey(id)) taskManager.historyManager.add(getSubTaskById(id));
                        }
                    } else {
                        taskFromString(line);
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e.getMessage());
            }
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasksHistory = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task oneTask : tasksHistory) {
            Integer id = oneTask.getId();
            sb.append(id.toString() + ",");
        }
        return sb.toString();
    }

    public Task taskFromString(String value) {
        String[] values = value.split(",");

        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];

        switch (type) {
            case TASK:
                Task task = new Task(id, type, name, status, description);
                taskMap.put(id, task);
                return task;
            case EPIC:
                Epic epic = new Epic(id, type, name, status, description);
                epicMap.put(id, epic);
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(values[5]);
                SubTask subTask = new SubTask(id, type, name, status, description, epicId);
                subTaskMap.put(id, subTask);
                return subTask;

        }
        return null;
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




    public static void main(String[] args) {
        File file = new File("resources/tasks.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.loadFromFile(file);

        System.out.println(fileBackedTaskManager.getHistoryManager().getHistory());
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println(fileBackedTaskManager.getAllSubTasks());
        System.out.println(fileBackedTaskManager.getAllEpics());

    }
}
