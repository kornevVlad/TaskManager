/*import manager.*;
import task.Task;
import task.Epic;
import task.SubTask;
import task.StatusTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException {

        TaskManager taskManager = Managers.getDefault();
        PrintManager printManager = new PrintManager();
*/
/*
        //Создание и сохранение таски
        Task task = new Task("Task1"," Descriptoin1", StatusTask.NEW);
        Task task1 = new Task("Task2","Descriptoin2",StatusTask.NEW);
        Task task2 = new Task("Task3","Descriptoin3",StatusTask.NEW);

        taskManager.addTasks(task);
        taskManager.addTasks(task1);
        taskManager.addTasks(task2);

        //Создание и сохранение эпика
        Epic epic = new Epic( "Epic1","Descriptoin1",StatusTask.NEW);
        Epic epic1 = new Epic( "Еpic2","Descriptoin2",StatusTask.NEW );

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);

        //Создание и сохранение сабтаски
        SubTask subTask = new SubTask("SubTask1","Descriptoin1",StatusTask.DONE,3);
        SubTask subTask1 = new SubTask("Subtask2","Descriptoin2",StatusTask.DONE,3);
        SubTask subTask2 = new SubTask("Subtask3","Descriptoin3",StatusTask.DONE,3);

        taskManager.addSubtasks(subTask);
        taskManager.addSubtasks(subTask1);
        taskManager.addSubtasks(subTask2);

        //----------------------------------------------
        // Запись Истории вызовов в файл

        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicByID(3);
        taskManager.getEpicByID(4);

       // taskManager.getTaskByID(1);
        taskManager.getIdSubTask(5);
        taskManager.getIdSubTask(6);
        taskManager.getIdSubTask(7);
     //  taskManager.deleteIdEpic(3);

     //   taskManager.getTaskByID(2);

      //  taskManager.getTaskByID(2);
        System.out.println(taskManager.getHistory());

        System.out.println("----------Восстановление данных из файла ---------------------");
          File file = new File("src/task.csv");
          taskManager.loadFromFile(file);
          System.out.println(taskManager.getHistory());
    }
}
*/