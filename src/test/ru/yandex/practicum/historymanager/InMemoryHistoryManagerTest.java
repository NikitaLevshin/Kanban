package ru.yandex.practicum.historymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest  {
    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task epic1;
    Task subTask1;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "task1", TaskStatus.NEW, "description");
        task2 = new Task(2, "task2", TaskStatus.NEW, "description");
        epic1 = new Epic(3, "epic1", "description");
        subTask1 = new SubTask(4, "subtask1", TaskStatus.NEW, "description", 3);
    }

    @Test
    public void addTest() {
        historyManager.add(task1);
        List<Task> testHistory = historyManager.getHistory();
        assertFalse(testHistory.isEmpty(), "История не добавилась");
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

        historyManager.remove(task1.getId());
        assertEquals(removalStartList, historyManager.getHistory(), "Задача с начала не удалилась");

        historyManager.remove(subTask1.getId());
        assertEquals(removalEndList, historyManager.getHistory(), "История из конца не удалилась");

        historyManager.add(task1);
        historyManager.remove(epic1.getId());
        assertEquals(removalInsideList, historyManager.getHistory(), "История из середины не удалилась");

    }

    @Test
    public void testGetHistory() {
        assertEquals(List.of(),historyManager.getHistory(), "История задач не пуста");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        List<Task> tasksTestList = List.of(task2, task1);
        assertEquals(tasksTestList, historyManager.getHistory(), "История записывается неккоректно");
    }

}