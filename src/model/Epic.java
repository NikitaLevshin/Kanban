package model;

import jdk.jshell.Snippet;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{

    final private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
        status = STATUS[statusKey()];
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public int statusKey() {
        HashMap<String, Integer> statuses = new HashMap<>();
        statuses.put(STATUS[0], 0);
        statuses.put(STATUS[1], 0);
        statuses.put(STATUS[2], 0);
            for (int i = 0; i < subTasks.size(); i++) {
                SubTask oneSubTask = subTasks.get(i);
                switch (oneSubTask.getStatus()) {
                    case "NEW":
                    statuses.put(STATUS[0], statuses.get(STATUS[0]) + 1);
                    break;
                    case "IN_PROGRESS":
                    statuses.put(STATUS[1], statuses.get(STATUS[1]) + 1);
                    break;
                    case "DONE":
                    statuses.put(STATUS[2], statuses.get(STATUS[2]) + 1);
                    break;
                }
            }
            if ((statuses.get(STATUS[0]) == subTasks.size()) || statuses == null) {
                return 0;
            } else if(statuses.get(STATUS[2]) == subTasks.size()) {
                return 2;
            } return 1;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subTasks=" + subTasks +
                '}';
    }
}
