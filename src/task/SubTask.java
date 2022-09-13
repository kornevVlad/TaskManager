package task;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, StatusTask status, String start, int duration ,int epicId ) {
        super(name, description, status, start, duration);
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
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration = " + duration +
                '}';
    }

    public TaskType getType(){
        return TaskType.SUBTASK;
    }


}