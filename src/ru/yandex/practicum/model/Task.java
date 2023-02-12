package ru.yandex.practicum.model;

import java.time.LocalDateTime;

public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected TaskStatus status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Long duration;
    protected LocalDateTime endTime;




    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        type = TaskType.TASK;
    }

    public Task(Integer id, String name, TaskStatus status, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        type = TaskType.TASK;
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = TaskType.TASK;
    }
    public Task(String name, String description, LocalDateTime startTime, Long duration) {
        this.name = name;
        this.description = description;
        type = TaskType.TASK;
        status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
        updateEndTime();
    }

    public Task(Integer id, String name, String description, LocalDateTime startTime, Long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        type = TaskType.TASK;
        status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
        updateEndTime();
    }
    public Task(Integer id, String name, TaskStatus status, String description, LocalDateTime startTime, Long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        type = TaskType.TASK;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        updateEndTime();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public TaskType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void updateEndTime() {
        if (startTime != null) endTime = startTime.plusMinutes(duration);
    }


    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", this.id, this.type, this.name, this.status, this.description, this.startTime, this.duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}