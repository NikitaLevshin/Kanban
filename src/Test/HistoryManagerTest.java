package Test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest <T extends HistoryManager> {
    T historyManager;
    Task task1;
    Task task2;
    Task epic1;
    Task subTask1;

    void setUp() {
        task1 = new Task(1, "task1", TaskStatus.NEW, "description");
        task2 = new Task(2, "task2", TaskStatus.NEW, "description");
        epic1 = new Epic(3, "epic1", "description");
        subTask1 = new SubTask(4, "subtask1", TaskStatus.NEW, "description", 3);
    }

    @Test
    public void addTest() {
        historyManager.add(task1);
        List<Task> testHistory = historyManager.getHistory();
        assertTrue(testHistory.size() != 0,"История не добавилась");
        historyManager.add(task1);
        assertEquals(1, testHistory.size(), "История не удалилась при перезаписи");
    }

    @Test
    public void removeTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subTask1);

        List<Task> removalStartList = List.of(task2, epic1, subTask1);
        List<Task> removalInsideList = List.of(task2, task1);
        List<Task> removalEndList = List.of(task2, epic1);

        historyManager.remove(1);
        assertEquals(removalStartList, historyManager.getHistory(), "Задача с начала не удалилась");

        historyManager.remove(4);
        assertEquals(removalEndList, historyManager.getHistory(), "История из конца не удалилась");

        historyManager.add(task1);
        historyManager.remove(3);
        assertEquals(removalInsideList, historyManager.getHistory(), "История из середины не удалилась");

    }

    @Test
    public void testGetHistory() {
        assertEquals(List.of(),historyManager.getHistory(), "История задач не пуста");
        List<Task> tasksTestList = List.of(task2, task1);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        assertEquals(tasksTestList, historyManager.getHistory(), "История записывается неккоректно");
    }

}