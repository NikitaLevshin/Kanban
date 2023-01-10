package ru.yandex.practicum.model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId, TaskType type) {
        super(name, description, type);
        this.epicId = epicId;
        this.status = TaskStatus.NEW;
    }
    public SubTask(Integer id, TaskType type, String name, TaskStatus status, String description, Integer epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,", this.id, this.type, this.name, this.status, this.description, this.getEpicId());
    }
}
