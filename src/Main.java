import manager.*;
import task.Task;
import task.Epic;
/*import task.SubTask;
import task.StatusTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException {

        TaskManager taskManager = Managers.getDefault();
        PrintManager printManager = new PrintManager();


        //Создание и сохранение таски
        Task task = new Task(2000,1,1,7,30,"Task1"," Descriptoin1", StatusTask.NEW,60);
        Task task1 = new Task(2000,2,1,7,30,"Task1"," Descriptoin1", StatusTask.NEW,60);
        Task task2 = new Task(2000,3,1,7,30,"Task1"," Descriptoin1", StatusTask.NEW,60);

        taskManager.addTasks(task);
        taskManager.addTasks(task1);
        taskManager.addTasks(task2);

        //Создание и сохранение эпика
        Epic epic = new Epic( 2000,1,2,3,00,"Epic1","Descriptoin1",StatusTask.NEW,60);
        Epic epic1 = new Epic( 2000,2,3,4,30,"Еpic2","Descriptoin2",StatusTask.NEW,60 );

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);

        //Создание и сохранение сабтаски
        SubTask subTask = new SubTask(2001,12,1,7,30,"SubTask1","Descriptoin1",StatusTask.DONE,3,60);
        SubTask subTask1 = new SubTask(2001,1,2,7,20,"Subtask2","Descriptoin2",StatusTask.DONE,3,80);
        SubTask subTask2 = new SubTask(2001,1,3,7,50,"Subtask3","Descriptoin3",StatusTask.DONE,3,90);

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