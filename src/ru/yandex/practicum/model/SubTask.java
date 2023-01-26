package ru.yandex.practicum.model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.status = TaskStatus.NEW;
        type = TaskType.SUBTASK;
    }
    public SubTask(Integer id, String name, TaskStatus status, String description, Integer epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, LocalDateTime startTime, Long duration, Integer epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
        this.status = TaskStatus.NEW;
    }

    public SubTask(Integer id, String name, String description, LocalDateTime startTime, Long duration, Integer epicId) {
        super(id, name, description, startTime, duration);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
        this.status = TaskStatus.NEW;
    }

    public SubTask(Integer id, String name, TaskStatus status, String description, LocalDateTime startTime, Long duration, Integer epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
        this.status = TaskStatus.NEW;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d", this.id, this.type, this.name, this.status, this.description, this.startTime, this.duration, this.getEpicId());
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
