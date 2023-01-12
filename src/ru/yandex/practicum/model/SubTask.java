package ru.yandex.practicum.model;

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

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s,%d,", this.id, this.type, this.name, this.status, this.description, this.getEpicId());
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
