package test.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    Task task1;
    Task task2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    Epic epic1;
    Epic epic2;

    void setUp() {
        task1 = new Task("task1", "description");
        task2 = new Task("task2", "description");
        epic1 = new Epic("epic1", "description");
        taskManager.newEpic(epic1);
        epic2 = new Epic("epic2", "description");
        taskManager.newEpic(epic2);
        subTask1 = new SubTask("subTask1", "description", epic1.getId());
        subTask2 = new SubTask("subTask2", "description", epic2.getId());
        subTask3 = new SubTask("subTask3", "description", epic1.getId());
    }


    @Test
    public void newTaskTest() {
        taskManager.newTask(task1);
        assertNotNull(taskManager.getTaskById(task1.getId()), "Задача не найдена");
    }

    @Test
    public void newSubTaskTest() {
        taskManager.newSubTask(subTask1);
        assertNotNull(taskManager.getSubTaskById(subTask1.getId()), "Сабтаск не найден");
    }

    @Test
    public void newEpicTest() {
        assertNotNull(taskManager.getEpicById(epic1.getId()), "Эпик не найден");
    }

    @Test
    public void getTaskTest() {
        taskManager.newTask(task1);
        assertEquals(TaskType.TASK, taskManager.getTaskById(task1.getId()).getType(), "Получили не задачу");
        assertNull(taskManager.getTaskById(123123), "Выводится задача при неверном ID");
    }

    @Test
    public void getSubTaskTest() {
        taskManager.newSubTask(subTask1);
        assertEquals(TaskType.SUBTASK, taskManager.getSubTaskById(subTask1.getId()).getType(), "Получили не сабтаск");
        assertNull(taskManager.getSubTaskById(123123), "Выводится задача при неверном ID");
    }

    @Test
    public void getEpicTest() {
        assertEquals(TaskType.EPIC, taskManager.getEpicById(epic1.getId()).getType(), "Получили не эпик");
        assertNull(taskManager.getEpicById(123123), "Выводится задача при неверном ID");
    }

    @Test
    public void subTaskInEpicTest() {
        taskManager.newSubTask(subTask1);
        assertEquals(epic1.getId(), subTask1.getEpicId(), "Сабтаск не в эпике");
    }

    @Test
    public void allTasksTest() {
        List<Task> tasks = List.of(task1, task2);
        taskManager.newTask(task1);
        taskManager.newTask(task2);
        assertEquals(tasks, taskManager.getAllTasks(), "Задачи не совпадают");
    }

    @Test
    public void allSubTasksTest() {
        List<SubTask> subTasks = List.of(subTask1, subTask2);
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask2);
        assertEquals(subTasks, taskManager.getAllSubTasks(), "Сабтаски не совпадают");
    }

    @Test
    public void allEpicsTest() {
        List<Epic> epics = List.of(epic1, epic2);
        assertEquals(epics, taskManager.getAllEpics(), "Эпики не совпадают");
    }

    @Test
    public void allSubTasksInEpicTest() {
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask3);
        List<Integer> subTasks = List.of(subTask1.getId(), subTask3.getId());
        assertEquals(subTasks, epic1.getSubTasks(), "Нет сабтасков в эпике");
    }

    @Test
    public void removeTaskTest() {
        taskManager.newTask(task1);
        int taskId = task1.getId();
        taskManager.removeTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId), "Задача не удалилась");
    }

    @Test
    public void removeSubTaskTest() {
        taskManager.newSubTask(subTask1);
        int taskId = subTask1.getId();
        taskManager.removeSubTaskById(taskId);
        assertNull(taskManager.getSubTaskById(taskId), "Сабтаск не удалился");
    }

    @Test
    public void removeEpicTest() {
        taskManager.newSubTask(subTask1);
        int epicId = epic1.getId();
        int subTaskId = subTask1.getId();
        taskManager.removeEpicById(epicId);
        assertNull(taskManager.getEpicById(epicId));
        assertNull(taskManager.getSubTaskById(subTaskId), "Эпик не удалился");
    }

    @Test
    public void removeTasksTest() {
        taskManager.newTask(task1);
        taskManager.newTask(task2);
        taskManager.removeTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не удалились");
    }

    @Test
    public void removeSubTasksTest() {
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask2);
        taskManager.removeSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Сабтаски не удалились");
    }

    @Test
    public void removeEpicsTest() {
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask2);
        taskManager.newSubTask(subTask3);
        taskManager.removeEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Эпики не удалились");
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Не удалились сабтаски при удалении эпиков");

    }

    @Test
    public void updateTaskTest() {
        taskManager.newTask(task1);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task1.getStatus(), "Статус задачи не изменился");
        task1.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task1.getStatus(), "Статус задачи на изменился");
    }

    @Test
    public void updateSubTaskTest() {
        taskManager.newSubTask(subTask1);
        taskManager.newSubTask(subTask3);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, subTask1.getStatus(), "Статус сабтаска не изменился");
        subTask1.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, subTask1.getStatus(), "Статус задачи не изменился");
    }

    @Test
    public void newTaskWithDurationTest() {
        Task testTask = new Task(3, "task", "description", LocalDateTime.of(2023,01,24,15,00), 40L);
        taskManager.newTask(testTask);
        assertNotNull(taskManager.getTaskById(testTask.getId()), "Задача не добавляется");
        assertEquals("2023-01-24T15:00", testTask.getStartTime().toString(), "Время отображается некорректно");
        assertEquals(40, testTask.getDuration(), "Продолжительность записывается некорректно");
        assertEquals("2023-01-24T15:40", testTask.getEndTime().toString(), "Время окончания считается некорректно");
    }

    @Test
    public void newSubTaskWithDurationTest() {
        SubTask testTask = new SubTask(3, "SubTask", "description",
                LocalDateTime.of(2023,01,24,15,00), 40L, 1);
        taskManager.newSubTask(testTask);
        assertNotNull(taskManager.getSubTaskById(testTask.getId()), "Задача не добавляется");
        assertEquals("2023-01-24T15:00", testTask.getStartTime().toString(), "Время отображается некорректно");
        assertEquals(40, testTask.getDuration(), "Продолжительность записывается некорректно");
        assertEquals("2023-01-24T15:40", testTask.getEndTime().toString(), "Время окончания считается некорректно");
    }

    @Test
    public void epicShouldEndLikeSubTaskWhenSubTaskEndIsLaterThenDuration() {
        taskManager.newSubTask(new SubTask(3, "SubTask", "description",
                    LocalDateTime.of(2023,01,24,14,00), 35L, 1));
        taskManager.newSubTask(new SubTask(4, "SubTask", "description",
                    LocalDateTime.of(2023,01,24,15,00), 40L, 1));
        assertEquals("2023-01-24T14:00", epic1.getStartTime().toString(), "Время начало эпика считается некорректно");
        assertEquals(100, epic1.getDuration(), "Время выполнения эпика считается некорректно");
        assertEquals("2023-01-24T15:40", epic1.getEndTime().toString(), "Время окончания эпика считается некорректно");
    }

    @Test
    public void shouldThrowExceptionWhenTasksIntersect() {
        final DateTimeException exception = assertThrows(
                DateTimeException.class,
                () -> {
                    taskManager.newTask(new Task(3, "task", "description", LocalDateTime.of(2023,01,24,15,00), 40L));
                    taskManager.newTask(new Task(4, "task", "description", LocalDateTime.of(2023,01,24,15,30), 20L));
                }
        );
        assertEquals("Время старта и конца заданий не должны пересекаться", exception.getMessage(), "Сообщение исключения неверно");
    }

    @Test
    public void prioritizedTasksSortingTest() {
        Task task1 = new Task(3, "task", "description", LocalDateTime.of(2023,01,24,15,00), 40L);
        Task task2 = new Task(4, "task", "description", LocalDateTime.of(2023,01,24,14,00), 40L);
        Task task3 = new Task(5, "task", "description", LocalDateTime.of(2023,01,24,16,00), 30L);
        taskManager.newTask(task1);
        taskManager.newTask(task2);
        taskManager.newTask(task3);
        Iterator<Task> testTasks = taskManager.getPrioritizedTasks().iterator();
        assertEquals(task2, testTasks.next(), "Задачи неправильньно сортируются");
        assertEquals(task1, testTasks.next(), "Задачи неправильно сортируются");
        assertEquals(task3, testTasks.next(), "Задачи неправильно сортируются");
    }

}
