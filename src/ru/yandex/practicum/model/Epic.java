package ru.yandex.practicum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task{

    private final ArrayList<Integer> subTasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
        status = TaskStatus.NEW;
        type = TaskType.EPIC;
    }
    public Epic (Integer id, String name, String description) {
        super(id, name, description);
        type = TaskType.EPIC;
        status = TaskStatus.NEW;
    }

    public void addSubTask(SubTask subTask) {
        if (!subTasks.contains(subTask.getId())) subTasks.add(subTask.getId());
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

    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", this.id, this.type, this.name, this.status, this.description, this.startTime, this.duration);
    }

    public void setEndTime(LocalDateTime time) {
        if (endTime == null || !time.isBefore(endTime)) this.endTime = time;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
