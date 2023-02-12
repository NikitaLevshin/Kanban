package ru.yandex.practicum.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.LocalDateTimeAdapter;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVTaskClient taskClient;

    public HttpTaskManager(String url) {
        super(null);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
        taskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        String tasks = gson.toJson(new ArrayList<>(getAllTasks()));
        taskClient.put("task", tasks);
        String epics = gson.toJson(new ArrayList<>(getAllEpics()));
        taskClient.put("epic", epics);
        String subTasks = gson.toJson(new ArrayList<>(getAllSubTasks()));
        taskClient.put("subTask", subTasks);
        String history = gson.toJson(new ArrayList<>(getHistoryManager().getHistory()));
        taskClient.put("history", history);
    }

    public void load() {
        List<Task> tasks = gson.fromJson(taskClient.load("task"), new TypeToken<List<Task>>() {
        }.getType());
        for (Task task : tasks) newTask(task);
        List<Epic> epics = gson.fromJson(taskClient.load("epic"), new TypeToken<List<Epic>>() {
        }.getType());
        for (Epic epic : epics) newEpic(epic);
        List<SubTask> subTasks = gson.fromJson(taskClient.load("subTask"), new TypeToken<List<SubTask>>() {
        }.getType());
        for (SubTask subTask : subTasks) newSubTask(subTask);
        List<Integer> historyIDs = gson.fromJson(taskClient.load("history"), new TypeToken<List<Integer>>() {
        }.getType());
        for (Integer id : historyIDs) {
            if (taskMap.containsKey(id)) historyManager.add(getTaskById(id));
            else if (epicMap.containsKey(id)) historyManager.add(getEpicById(id));
            else if (subTaskMap.containsKey(id)) historyManager.add(getSubTaskById(id));
        }
    }
}
