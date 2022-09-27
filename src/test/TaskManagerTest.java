package test;

import manager.TaskManager;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;

abstract class TaskManagerTest <T extends TaskManager> {

    public T taskManager;

    public void saveTask() {
        Task taskId0New = new Task("Task1", " Descriptoin1", StatusTask.NEW, "01.02.2000 07:00", 60);
        Task taskId1New = new Task("Task2", " Descriptoin1", StatusTask.DONE, "01.02.2000 08:00", 60);
        taskManager.addTasks(taskId0New);
        taskManager.addTasks(taskId1New);

        Epic epicId2New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        Epic epicId3New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        taskManager.addEpic(epicId2New);
        taskManager.addEpic(epicId3New);

        SubTask subTaskId4New = new SubTask("SubTask1", "Descriptoin1", StatusTask.NEW, "01.02.2000 12:00", 60, 3);
        SubTask subTaskId5Done = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 10:00", 60, 3);
        SubTask subTaskId6Done = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 11:00", 60, 3);
        taskManager.addSubtasks(subTaskId4New);
        taskManager.addSubtasks(subTaskId5Done);
        taskManager.addSubtasks(subTaskId6Done);
    }

    public void clearTaskManager(){
        taskManager.deleteTask();
        taskManager.deleteEpicAndSubtask();
    }
}