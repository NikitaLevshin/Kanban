package ru.yandex.practicum.model;

import ru.yandex.practicum.manager.TaskManager;

import java.util.ArrayList;

public class Epic extends Task{

    private final ArrayList<Integer> subTasks = new ArrayList<>();

    private TaskStatus status;

    public Epic(String name, String description) {
        super(name, description);
        status = TaskStatus.NEW;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask.getId());
    }

    public void updateStatus(ArrayList<SubTask> subTask) {
        int countNew = 0;
        int countDone = 0;
        for (int i = 0; i < subTask.size(); i++) {
            SubTask oneSubTask = subTask.get(i);
                switch (oneSubTask.getStatus()) {
                    case NEW:
                        countNew++;
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

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void removeSubTasks() {
        subTasks.clear();
    }

    public void removeOneSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
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
