import manager.*;
import task.Task;
import task.Epic;
import task.SubTask;
import task.StatusTask;

public class Main {
    public static void main(String[]args){

        TaskManager taskManager = Managers.getDefault();
        PrintManager printManager = new PrintManager();

        //Создание и сохранение таски
        Task task = new Task("Z1","1", StatusTask.NEW);
        Task task1 = new Task("Z2","2",StatusTask.NEW);
        Task task2 = new Task("Z3","3",StatusTask.NEW);

        taskManager.addTasks(task);
        taskManager.addTasks(task1);
        taskManager.addTasks(task2);

        //Создание и сохранение эпика
        Epic epic = new Epic( "Ep1"," Э1",StatusTask.NEW);
        Epic epic1 = new Epic( "Еp2","Э2",StatusTask.NEW );

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);

        //Создание и сохранение сабтаски
        SubTask subTask = new SubTask("S1","S1",StatusTask.DONE,3);
        SubTask subTask1 = new SubTask("S2","S2",StatusTask.DONE,3);
        SubTask subTask2 = new SubTask("S3","S3",StatusTask.DONE,3);

        taskManager.addSubtasks(subTask);
        taskManager.addSubtasks(subTask1);
        taskManager.addSubtasks(subTask2);

        //----------------------------------------------
        // Проверка Истории вызова Задач


       taskManager.getEpicByID(3);
        taskManager.getIdSubTask(5);
        taskManager.getIdSubTask(6);
        taskManager.getIdSubTask(7);
        taskManager.getEpicByID(3);
        System.out.println(taskManager.getHistory());

        taskManager.getTaskByID(1);

        System.out.println(taskManager.getHistory());  // Получение списка Истории
        taskManager.getTaskByID(0);
        taskManager.deleteIdEpic(3);
        System.out.println(taskManager.getHistory());  // Получение списка Истории

        taskManager.getEpicByID(3);
        taskManager.getTaskByID(2);

        System.out.println(taskManager.getHistory());  // Получение списка Истории

        taskManager.getTaskByID(1);
        taskManager.getEpicByID(3);
        System.out.println(taskManager.getHistory());  // Получение списка Истории

        taskManager.deleteIdEpic(3);

        System.out.println(taskManager.getHistory());  // Получение списка Истории

        //--------------------------------
    }
}