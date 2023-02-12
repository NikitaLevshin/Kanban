package test.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.KVServer;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.TaskStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager testManager;
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        testManager = new InMemoryTaskManager();
        epic = new Epic("Epic", "description");
        testManager.newEpic(epic);
        subTask1 = new SubTask("Subtask", "description", epic.getId());
        subTask2 = new SubTask("Subtask2", "description", epic.getId());

    }
    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }
    @Test
    public void epicStatusSubTasksEmptyList() {
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус пустого эпика - NEW");
    }

    @Test
    public void epicNewWhenAllSubTasksNew() {
        testManager.newSubTask(subTask1);
        testManager.newSubTask(subTask2);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика со всеми сабтасками NEW должен быть NEW");
    }

    @Test
    public void epicDoneWhenAllSubTasksDone() {
        testManager.newSubTask(subTask1);
        testManager.newSubTask(subTask2);
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        testManager.updateSubTask(subTask1);
        testManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика со всеми сабтасками DONE должен быть DONE");
    }

    @Test
    public void epicInProgressWhenSubTasksNewDone() {
        testManager.newSubTask(subTask1);
        testManager.newSubTask(subTask2);
        subTask2.setStatus(TaskStatus.DONE);
        testManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика с сабтсками NEW-DONE должен быть IN_PROGRESS");
    }

    @Test
    public void epicInProgressWhenSubTasksInProgress() {
        testManager.newSubTask(subTask1);
        testManager.newSubTask(subTask2);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        testManager.updateSubTask(subTask1);
        testManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика с сабтсками IN PROGRESS должен" +
                                                                "быть IN PROGRESS");
    }
}