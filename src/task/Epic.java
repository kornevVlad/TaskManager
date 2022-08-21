package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
   private List<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);

    }

    public void addSubtask(int subtaskId){
        this.subtaskId.add(subtaskId);
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}