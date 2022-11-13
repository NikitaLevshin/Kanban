package model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int id, int epicId) {
        super(name, description, id);
        this.epicId = epicId;
        status = STATUS[0];
    }

    public int getEpicId() {
        return epicId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(int statusKey) {
        status = STATUS[statusKey];
    }

    @Override
    public String toString() {
        return "model.SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
