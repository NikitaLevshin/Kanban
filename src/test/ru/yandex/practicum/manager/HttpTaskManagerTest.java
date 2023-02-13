package ru.yandex.practicum.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.HttpTaskManager;
import ru.yandex.practicum.api.KVServer;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;


    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager("http://localhost:8080/");
        task1 = new Task("task", "description", LocalDateTime.of(2023,02,24,15,00), 40L);
        task2 = new Task("task2", "description");
        epic1 = new Epic("epic1", "description");
        taskManager.newEpic(epic1);
        epic2 = new Epic("epic2", "description");
        taskManager.newEpic(epic2);
        subTask1 = new SubTask("SubTask", "description",
                LocalDateTime.of(2023,02,24,16,00), 45L, epic1.getId());
        subTask2 = new SubTask("subTask2", "description", epic2.getId());
        subTask3 = new SubTask("subTask3", "description", epic1.getId());
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void testLoadFromManager() {
        taskManager.newTask(task1);
        taskManager.newSubTask(subTask1);
        taskManager.load();
        assertEquals(task1.getId(), taskManager.getTaskById(task1.getId()).getId());
        assertEquals(epic1.getId(), taskManager.getEpicById(epic1.getId()).getId());
        assertEquals(subTask1.getId(), taskManager.getSubTaskById(subTask1.getId()).getId());
    }

}