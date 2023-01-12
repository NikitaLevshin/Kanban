package ru.yandex.practicum.model;

public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected TaskStatus status;
    protected TaskType type;




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

    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s,", this.id, this.type, this.name, this.status, this.description);
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