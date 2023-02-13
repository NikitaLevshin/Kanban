package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.HttpTaskServer;
import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.*;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;
    Gson gson;
    TaskManager taskManager;
    @BeforeEach
    public void beforeEach() throws IOException {
      httpTaskServer = new HttpTaskServer(new File("testresources/httpTestTasks.csv"));
      httpTaskServer.serverStart();
      gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
      taskManager = new FileBackedTaskManager(new File("testresources/httpTestTasks.csv"));
      epic1 = new Epic("epic1", "description");
      taskManager.newEpic(epic1);
      epic2 = new Epic("epic2", "description");
      taskManager.newEpic(epic2);
      task1 = new Task("task", "description", LocalDateTime.of(2023,02,24,15,00), 40L);
      taskManager.newTask(task1);
      task2 = new Task("task2", "description");
      taskManager.newTask(task2);
      subTask1 = new SubTask("SubTask", "description",
                LocalDateTime.of(2023,02,24,16,00), 45L, epic1.getId());
      taskManager.newSubTask(subTask1);
      subTask2 = new SubTask("SubTask", "description", epic1.getId());
      taskManager.newSubTask(subTask2);

    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.serverStop();
    }

    @Test
    public void allTasksGetRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> allTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertNotNull(allTasks, "Задачи не получены");
        assertEquals(taskManager.getAllTasks().size(), allTasks.size(), "Загружены не все задачи");
        assertEquals(task1.getId(), allTasks.get(0).getId(), "Задачи не совпадают");
    }

    @Test
    public void getTaskByIdRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(task1.getId(), task.getId(), "Задачи не совпадают");
    }

    @Test
    public void deleteAllTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> allTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(0, allTasks.size(),"Задачи не удалились");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> allTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());

        assertNotEquals(allTasks.contains(task1), true, "Задача не удалена");
    }

    @Test
    public void postTask() throws IOException, InterruptedException {
        Task testTask = new Task("testTask", "description", LocalDateTime.of(2023, 04, 15, 17, 00), 50L);
        taskManager.newTask(testTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String task = gson.toJson(testTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=" + testTask.getId());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task oneTask = gson.fromJson(response.body(), Task.class);

        assertEquals(testTask.getId(), oneTask.getId(), "Задачи не совпадают");
    }

    @Test
    public void getAllSubTasksRequestTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<SubTask> allSubTasks = gson.fromJson(response.body(), new TypeToken<List<SubTask>>(){}.getType());
        assertNotNull(allSubTasks, "Задачи не получены");
        assertEquals(taskManager.getAllSubTasks().size(), allSubTasks.size(), "Загружены не все задачи");
        assertEquals(subTask1.getId(), allSubTasks.get(0).getId(), "Задачи не совпадают");
    }

    @Test
    public void getSubTasksInEpicRequestTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> subTasksId = gson.fromJson(response.body(), new TypeToken<List<SubTask>>(){}.getType());
        assertEquals(taskManager.getAllSubTasksInEpic(epic1.getId()).size(), subTasksId.size(), "Количество подзадач не совпадает");
        assertEquals(subTask1.getId(), subTasksId.get(0).getId(), "Задачи не совпадают");
    }

    @Test
    public void getAllEpicsTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());
        assertEquals(taskManager.getAllEpics().size(), epics.size(), "Эпики загружены некорректно");
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        taskManager.getTaskById(task1.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic2.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertNotNull(history, "История не вернулась");
        assertEquals(taskManager.getHistoryManager().getHistory().get(0).getId(), history.get(0).getId(),
                "История возвращается некорректно");
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> prioritizedTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(taskManager.getPrioritizedTasks().get(1).getId(), prioritizedTasks.get(0).getId(),
        "Задачи возвращаются некорректно");
    }
}