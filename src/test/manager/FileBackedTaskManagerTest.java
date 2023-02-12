package test.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.KVServer;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    final static File file = new File("resources/testTasks.csv");
    KVServer kvServer;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new FileBackedTaskManager(file);
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
    void afterEach() {
        kvServer.stop();
    }

    @Test
    public void emptySaveTest() {
        File emptyFile = new File("resources/emptyTasks.csv");
        FileBackedTaskManager testSave = new FileBackedTaskManager(emptyFile);
        testSave.save();
        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(emptyFile);
        assertTrue(loadFromFile.getAllTasks().isEmpty(), "Задачи не пусты");
        assertTrue(loadFromFile.getAllSubTasks().isEmpty(), "Сабтаски не пусты");
        assertTrue(loadFromFile.getAllEpics().isEmpty(), "Эпики не пусты");
    }

    @Test
    public void saveAndLoadTest() {
        taskManager.newTask(task1);
        taskManager.newSubTask(subTask1);
        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task1.getId(), loadFromFile.getTaskById(task1.getId()).getId(), "Некорректная загрузка файла");
        assertEquals(epic1.getId(), loadFromFile.getEpicById(epic1.getId()).getId(), "Некорректная загрузка файла");
        assertEquals(subTask1.getId(), loadFromFile.getSubTaskById(subTask1.getId()).getId(), "Некорректная загрузка файла");
    }

    @Test
    public void saveAndLoadHistoryTest() {
        taskManager.newTask(task1);
        taskManager.getTaskById(task1.getId());
        taskManager.newSubTask(subTask1);
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic1.getId());

        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);

        List<Task> history = loadFromFile.getHistoryManager().getHistory();
        assertEquals(task1.getId(), history.get(0).getId(), "История загружается некорректно");
        assertEquals(epic1.getId(), history.get(1).getId(), "История загружается некорректно");
        assertEquals(subTask1.getId(), history.get(2).getId(), "История загружается некорректно");
    }

    @Test
    public void shouldThrowManagerSaveExceptionWhenSaveToNonExistentFile() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    FileBackedTaskManager saveToFile = FileBackedTaskManager.loadFromFile(new File("resources/nonExistent.csv"));
                    saveToFile.save();
                }
        );
        assertEquals("Не удалось сохранить/загрузить файл", exception.getMessage(), "Исключение не выбросилось");
    }
}