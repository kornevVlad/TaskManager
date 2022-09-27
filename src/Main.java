
import manager.Managers;
import manager.TaskManager;

import task.Epic;
import task.StatusTask;
import server.KVServer;
import task.SubTask;
import task.Task;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException{

        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager taskManager = Managers.getDefault();

        Task taskId0New = new Task("Task1", " Descriptoin1", StatusTask.NEW, "01.02.2000 08:00", 60);
        Task taskId1New = new Task("Task2", " Descriptoin1", StatusTask.DONE, "01.02.2000 07:00", 60);
        taskManager.addTasks(taskId0New);
        taskManager.addTasks(taskId1New);

        taskManager.save(); //Сохранение на сервер

        Epic epicId2New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        Epic epicId3New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        taskManager.addEpic(epicId2New);
        taskManager.addEpic(epicId3New);

        Task updateTask = new Task("TaskUPDATE", " DescriptoinUPDATE", StatusTask.NEW, "01.02.2000 07:00", 60);
        taskManager.updatingTask(0, updateTask);

        SubTask subTaskId4New = new SubTask("SubTask1", "Descriptoin1", StatusTask.NEW, "01.02.2000 12:00", 60, 3);
        SubTask subTaskId5Done = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 10:00", 60, 3);
        SubTask subTaskId6Done = new SubTask("Subtask3", "Descriptoin2", StatusTask.DONE, "01.02.2000 11:00", 60, 3);
        taskManager.addSubtasks(subTaskId4New);
        taskManager.addSubtasks(subTaskId5Done);
        taskManager.addSubtasks(subTaskId6Done);
        taskManager.getTaskByID(0); // Запись в историю просмотра
        taskManager.getIdSubTask(4);
        taskManager.save();

        System.out.println(taskManager.getHistory());
        taskManager.save();
        taskManager.load();


        System.out.println("Проверка данных на сервере---------------");
        for (String data: kvServer.getData().values()) {
          System.out.println(data);
        }

        System.out.println();
        System.out.println("Полученные данные из сервера-------------");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpic());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getHistory());
    }
}