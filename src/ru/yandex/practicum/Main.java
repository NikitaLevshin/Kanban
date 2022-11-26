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

        TaskManager taskManager = new InMemoryTaskManager();
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

        //Выводим списки
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistoryManager().getHistory());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getHistoryManager().getHistory());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistoryManager().getHistory());
        System.out.println(taskManager.getAllSubTasksInEpic(vacation));

        //Удаляем сабтаск по айди
        taskManager.removeSubTaskById(collectionID);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistoryManager().getHistory());

        //Проверяем изменение статусов у эпиков
        tourBuy.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(tourBuy);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistoryManager().getHistory());

        taskManager.removeEpicById(vacationID);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getHistoryManager().getHistory());

        shopping.setStatus(TaskStatus.DONE);
        taskManager.updateEpic(shopping);
        System.out.println(shopping);

        clothes.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(clothes);
        System.out.println(shopping);

        //Удаляем все сабтаски
        taskManager.removeSubTasks();
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getHistoryManager().getHistory());
    }
}
