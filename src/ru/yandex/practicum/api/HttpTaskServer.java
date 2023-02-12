package ru.yandex.practicum.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.LocalDateTimeAdapter;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final Gson gson;
    private final TaskManager taskManager;


    public HttpTaskServer(File file) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        taskManager = FileBackedTaskManager.loadFromFile(file);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
        httpServer.createContext("/tasks", this::tasksHandler);
    }

    private void tasksHandler(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (path.contains("tasks/task")) {
                handleTask(httpExchange);
            } else if (path.contains("subtask/epic")) {
                handleSubTaskEpic(httpExchange);
            } else if (path.contains("subtask")) {
                handleSubTask(httpExchange);
            } else if (path.contains("epic")) {
                handleEpic(httpExchange);
            } else if (path.contains("history")) {
                handleHistory(httpExchange);
            } else if (path.endsWith("tasks/")) {
                handleAllTasks(httpExchange);
            } else {
                System.out.println("Неверный запрос");
                sendResponse(httpExchange, "Неверный запрос", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTask(HttpExchange httpExchange) throws IOException {
        Task task;
        String response;

        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    List<Task> tasks = taskManager.getAllTasks();
                    response = gson.toJson(tasks);
                } else {
                        task = taskManager.getTaskById(Integer.parseInt(httpExchange.getRequestURI()
                                .getQuery()
                                .split("=")[1]));
                        response = gson.toJson(task);

                }
                sendResponse(httpExchange, response, 200);
                break;
            case "POST":
                String jsonTask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (jsonTask.isEmpty()) {
                    System.out.println("Текст задачи пуст");
                    sendResponse(httpExchange, "Текст задачи пуст", 406);
                } else {
                    task = gson.fromJson(jsonTask, Task.class);
                    if (taskManager.getTaskById(task.getId()) != null) {
                        taskManager.updateTask(task);
                        response = "Задача обновлена";
                        System.out.println(response);
                    } else {
                        taskManager.newTask(task);
                        response = "Задача создана";
                        System.out.println(response);
                    }
                    sendResponse(httpExchange, response, 200);
                }
                break;
            case "DELETE":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    taskManager.removeTasks();
                    response = "Все задачи удалены";
                } else {
                    taskManager.removeTaskById(Integer.parseInt(httpExchange.getRequestURI()
                            .getQuery()
                            .split("=")[1]));
                    response = "Задача удалена";
                }
                sendResponse(httpExchange, response, 200);
                break;
            default:
                sendResponse(httpExchange,"Неверный метод", 405);
        }
    }
    private void handleSubTaskEpic(HttpExchange httpExchange) throws IOException {
        int epicId;
        String response;
        if (httpExchange.getRequestMethod().equals("GET")) {
            epicId = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            List<SubTask> subTasks = taskManager.getAllSubTasksInEpic(epicId);
            response = gson.toJson(subTasks);
            sendResponse(httpExchange, response, 200);
        } else {
            response = "Неверный метод";
            sendResponse(httpExchange, response, 405);
        }
    }
    private void handleSubTask(HttpExchange httpExchange) throws IOException {
        SubTask subTask;
        String response;

        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    List<SubTask> subTasks = taskManager.getAllSubTasks();
                    response = gson.toJson(subTasks);
                } else {
                    subTask = taskManager.getSubTaskById(Integer.parseInt(httpExchange.getRequestURI()
                            .getQuery()
                            .split("=")[1]));
                    response = gson.toJson(subTask);

                }
                sendResponse(httpExchange, response, 200);
                break;
            case "POST":
                String jsonSubTask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (jsonSubTask.isEmpty()) {
                    System.out.println("Текст задачи пуст");
                    sendResponse(httpExchange, "Текст задачи пуст", 406);
                } else {
                    subTask = gson.fromJson(jsonSubTask, SubTask.class);
                    if (taskManager.getSubTaskById(subTask.getId()) != null) {
                        taskManager.updateSubTask(subTask);
                        response = "Подзадача обновлена";
                        System.out.println(response);
                    } else {
                        taskManager.newSubTask(subTask);
                        response = "Подзадача создана";
                        System.out.println(response);
                    }
                    sendResponse(httpExchange, response, 201);
                }
                break;
            case "DELETE":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    taskManager.removeSubTasks();
                    response = "Все подзадачи удалены";
                } else {
                    taskManager.removeSubTaskById(Integer.parseInt(httpExchange.getRequestURI()
                            .getQuery()
                            .split("=")[1]));
                    response = "Подзадача удалена";
                }
                sendResponse(httpExchange, response, 200);
                break;
            default:
                sendResponse(httpExchange,"Неверный метод", 405);
        }
    }
    private void handleEpic(HttpExchange httpExchange) throws IOException {
        Epic epic;
        String response;

        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    List<Epic> epics = taskManager.getAllEpics();
                    response = gson.toJson(epics);
                } else {
                    epic = taskManager.getEpicById(Integer.parseInt(httpExchange.getRequestURI()
                            .getQuery()
                            .split("=")[1]));
                    response = gson.toJson(epic);

                }
                sendResponse(httpExchange, response, 200);
                break;
            case "POST":
                String jsonEpic = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (jsonEpic.isEmpty()) {
                    System.out.println("Текст задачи пуст");
                    sendResponse(httpExchange, "Текст задачи пуст", 406);
                } else {
                    epic = gson.fromJson(jsonEpic, Epic.class);
                    if (taskManager.getEpicById(epic.getId()) != null) {
                        taskManager.updateEpic(epic);
                        response = "Эпик обновлен";
                        System.out.println(response);
                    } else {
                        taskManager.newEpic(epic);
                        response = "Эпик создан";
                        System.out.println(response);
                    }
                    sendResponse(httpExchange, response, 201);
                }
                break;
            case "DELETE":
                if (httpExchange.getRequestURI().getQuery() == null) {
                    taskManager.removeEpics();
                    response = "Все эпики удалены";
                } else {
                    taskManager.removeEpicById(Integer.parseInt(httpExchange.getRequestURI()
                            .getQuery()
                            .split("=")[1]));
                    response = "Эпик удалён";
                }
                sendResponse(httpExchange, response, 200);
                break;
            default:
                sendResponse(httpExchange,"Неверный метод", 405);
        }
    }
    private void handleHistory(HttpExchange httpExchange) throws IOException {
        String response;
        if (httpExchange.getRequestMethod().equals("GET")) {
            List<Task> history = taskManager.getHistoryManager().getHistory();
            response = gson.toJson(history);
            sendResponse(httpExchange, response, 200);
        } else {
            response = "Неверный метод";
            sendResponse(httpExchange, response, 405);
        }
    }
    private void handleAllTasks(HttpExchange httpExchange) throws IOException {
        String response;
        if (httpExchange.getRequestMethod().equals("GET")) {
            List<Task> tasks = taskManager.getPrioritizedTasks();
            response = gson.toJson(tasks);
            sendResponse(httpExchange, response, 200);
        } else {
            response = "Неверный метод";
            sendResponse(httpExchange, response, 405);
        }
    }

    protected void sendResponse(HttpExchange httpExchange, String response, int responseCode) throws IOException {
        httpExchange.sendResponseHeaders(responseCode, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        httpExchange.close();
    }

    public void serverStart() {
        httpServer.start();
    }
    public void serverStop() {
        httpServer.stop(0);
    }
}
