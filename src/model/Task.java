package model;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected String status;
    protected final String[] STATUS = new String[] {"NEW", "IN_PROGRESS", "DONE"};


    public void setStatus(int statusKey) {
        status = STATUS[statusKey];
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = STATUS[0];

    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}