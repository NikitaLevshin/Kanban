package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    final static File file = new File("resources/testTasks.csv");

    @BeforeEach
    void BeforeEach() {
        taskManager = new FileBackedTaskManager(file);
    }

    void setUp() {
        task1 = new Task(10, "task", "description", LocalDateTime.of(2023,01,24,15,00), 40L);
        taskManager.newTask(task1);
        task2 = new Task("task2", "description");
        taskManager.newTask(task2);
        epic1 = new Epic("epic1", "description");
        taskManager.newEpic(epic1);
        epic2 = new Epic("epic2", "description");
        taskManager.newEpic(epic2);
        subTask1 = new SubTask(11, "SubTask", "description",
                LocalDateTime.of(2023,01,24,16,00), 45L, 3);
        taskManager.newSubTask(subTask1);
        subTask2 = new SubTask("subTask2", "description", epic2.getId());
        taskManager.newSubTask(subTask2);
        subTask3 = new SubTask("subTask3", "description", epic1.getId());
        taskManager.newSubTask(subTask3);
    }

    @Test
    public void emptySaveTest() {
        taskManager.save();
        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);
        assertTrue(loadFromFile.getAllTasks().isEmpty(), "Задачи не пусты");
        assertTrue(loadFromFile.getAllSubTasks().isEmpty(), "Сабтаски не пусты");
        assertTrue(loadFromFile.getAllEpics().isEmpty(), "Эпики не пусты");
    }

    @Test
    public void saveAndLoadTest() {
        setUp();
        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task1.getId(), loadFromFile.getTaskById(task1.getId()).getId(), "Некорректная загрузка файла");
        assertEquals(epic1.getId(), loadFromFile.getEpicById(epic1.getId()).getId(), "Некорректная загрузка файла");
        assertEquals(subTask1.getId(), loadFromFile.getSubTaskById(subTask1.getId()).getId(), "Некорректная загрузка файла");
    }

    @Test
    public void saveAndLoadHistoryTest() {
        setUp();
        taskManager.getTaskById(task1.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic1.getId());

        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(file);

        List<Task> history = loadFromFile.getHistoryManager().getHistory();
        assertEquals(task1.getId(), history.get(0).getId(), "История загружается некорректно");
        assertEquals(epic1.getId(), history.get(1).getId(), "История загружается некорректно");
        assertEquals(subTask1.getId(), history.get(2).getId(), "История загружается некорректно");
    }
}