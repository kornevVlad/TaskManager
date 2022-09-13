package task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
   private List<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status,null, 0);
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
    public TaskType getType(){
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }
}