package ru.yandex.practicum.model;

import java.util.ArrayList;

public class Epic extends Task{

    private final ArrayList<Integer> subTasks = new ArrayList<>();


    public Epic(String name, String description, TaskType type) {
        super(name, description, type);
        status = TaskStatus.NEW;
    }
    public Epic (Integer id, TaskType type, String name, TaskStatus status, String description) {
        super(id, type, name, status, description);
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
        return String.format("%d,%s,%s,%s,%s,", this.id, this.type, this.name, this.status, this.description);
    }
}
