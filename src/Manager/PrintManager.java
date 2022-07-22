package Manager;

import Task.Task;
import Task.SubTask;
import java.util.ArrayList;
import Task.Epic;

public class PrintManager {

    public void printTask(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void printEpic(ArrayList<Epic> epics) {
        for (Epic epic : epics) {
            System.out.println(epic);
        }
    }

    public void printSubtask(ArrayList<SubTask> subTasks) {
        for (SubTask subtask : subTasks) {
            System.out.println(subtask);
        }
    }
}
