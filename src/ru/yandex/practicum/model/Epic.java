package ru.yandex.practicum.model;

import ru.yandex.practicum.manager.TaskManager;

import java.util.ArrayList;

public class Epic extends Task{

    private final ArrayList<SubTask> subTasks = new ArrayList<>();
    /* оставил здесь в списке subtask, т.к. не знаю как получить все сабтаски здесь только по id
        для метода updateStatus
     */
    private TaskStatus status;

    public Epic(String name, String description) {
        super(name, description);
        status = TaskStatus.NEW;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void updateStatus() {
        int countNew = 0;
        int countDone = 0;
        for (int i = 0; i < subTasks.size(); i++) {
            SubTask oneSubTask = subTasks.get(i);
                switch (oneSubTask.getStatus()) {
                    case NEW:
                        countNew++;
                        break;
                    case IN_PROGRESS:
                        break;
                    case DONE:
                        countDone++;
                        break;
                }
        }
            if (countNew == subTasks.size()) {
                status = TaskStatus.NEW;
            } else if (countDone == subTasks.size()) {
                status = TaskStatus.DONE;
            } else {
                status = TaskStatus.IN_PROGRESS;
            }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeSubTasks() {
        subTasks.clear();
    }

    public void removeOneSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    public ArrayList<Integer> getSubTasksId() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < subTasks.size(); i++) {
            result.add(subTasks.get(i).getId());
        }
        return result;
    }

    @Override
    public String toString() {
        return "ru.yandex.practicum.model.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subTasks=" + subTasks +
                '}';
    }
}
