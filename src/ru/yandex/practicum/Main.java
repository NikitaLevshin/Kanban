package ru.yandex.practicum;

import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

public class Main {
    public static void main(String[] args) {
        //Тестирование
        //Создаем объекты задач, подзадач, и эпиков

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task movement = new Task("Переезд", "Переезжаем в новую квартиру");
        int movementID = taskManager.newTask(movement);
        Task cleaning = new Task ("Уборка", "Убираем квартиру");
        int cleaningID = taskManager.newTask(cleaning);
        Epic vacation = new Epic("Отдых заграницей", "Планирование отдыха");
        int vacationID = taskManager.newEpic(vacation);
        Epic shopping = new Epic("Покупка вещей", "Вещи для отдыха");
        int shoppingID = taskManager.newEpic(shopping);
        SubTask tourBuy = new SubTask("Покупка путевки", "Выбор отеля", vacation.getId());
        int tourBuyID = taskManager.newSubTask(tourBuy);
        SubTask collection = new SubTask("Сбор вещей", "Собираем чемоданы", vacation.getId());
        int collectionID = taskManager.newSubTask(collection);
        SubTask clothes = new SubTask("Поездка в ТЦ", "Покупаем вещи в ТЦ", shopping.getId());
        int clothesID = taskManager.newSubTask(clothes);

        //Выводим таски и историю
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        System.out.println(taskManager.historyManager.getHistory());

        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        System.out.println(taskManager.historyManager.getHistory());

        taskManager.removeSubTaskById(5);
        System.out.println(taskManager.historyManager.getHistory());

        taskManager.getEpicById(4);
        System.out.println(taskManager.historyManager.getHistory());

    }
}
