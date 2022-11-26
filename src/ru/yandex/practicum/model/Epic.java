package ru.yandex.practicum.model;

import java.util.ArrayList;

public class Epic extends Task{

    private final ArrayList<Integer> subTasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
        status = TaskStatus.NEW;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask.getId());
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
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subTasks=" + subTasks +
                '}';
    }
}
