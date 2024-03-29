package task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
   private List<Integer> subtaskId;

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status,null, 0);
        this.subtaskId = new ArrayList<>();
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
                ", startTime =" + startTime +
                ", duration =" + duration +
                '}';
    }
    public TaskType getType() {
        return TaskType.EPIC;
    }

}