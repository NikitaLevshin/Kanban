package ru.yandex.practicum;

import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.*;

public class Main {
    public static void main(String[] args) {
        //Тестирование
        //Создаем объекты задач, подзадач, и эпиков

        TaskManager taskManager = Managers.getDefault();
        Task movement = new Task("Переезд", "Переезжаем в новую квартиру", TaskType.TASK);
        int movementID = taskManager.newTask(movement);
        Task cleaning = new Task ("Уборка", "Убираем квартиру", TaskType.TASK);
        int cleaningID = taskManager.newTask(cleaning);
        Epic vacation = new Epic("Отдых заграницей", "Планирование отдыха", TaskType.EPIC);
        int vacationID = taskManager.newEpic(vacation);
        Epic shopping = new Epic("Покупка вещей", "Вещи для отдыха", TaskType.EPIC);
        int shoppingID = taskManager.newEpic(shopping);
        SubTask tourBuy = new SubTask("Покупка путевки", "Выбор отеля", vacation.getId(), TaskType.SUBTASK);
        int tourBuyID = taskManager.newSubTask(tourBuy);
        SubTask collection = new SubTask("Сбор вещей", "Собираем чемоданы", vacation.getId(), TaskType.SUBTASK);
        int collectionID = taskManager.newSubTask(collection);
        SubTask clothes = new SubTask("Поездка в ТЦ", "Покупаем вещи в ТЦ", vacation.getId(), TaskType.SUBTASK);
        int clothesID = taskManager.newSubTask(clothes);

        taskManager.getEpicById(4);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);

        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);

        taskManager.getTaskById(2);
        taskManager.getEpicById(4);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);

    }
}
