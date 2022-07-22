import Manager.PrintManager;
import Manager.TaskManager;
import Task.Task;
import Task.Epic;
import Task.SubTask;

import static Task.StatusTask.NEW;
import static Task.StatusTask.IN_PROGRES;
import static Task.StatusTask.DONE;


public class Main {
    public static void main(String[]args){

        TaskManager taskManager = new TaskManager();
        PrintManager printManager = new PrintManager();

        Task task = new Task("Убрать квартику","Убрать в кухне", NEW);
        Task task1 = new Task("Выгулять собаку","Помыть лапы",NEW);

        taskManager.addTasks(task);
        taskManager.addTasks(task1);

        Epic epic = new Epic( "Первая цель","Первый выполнять",NEW);
        Epic epic1 = new Epic( "Вторая цель","Второй выполнять",NEW );

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);


        SubTask subTask = new SubTask("К перовой цели","Дополни выполнение",DONE,0);
        SubTask subTask1 = new SubTask("К второй цели","Дополни выполнение",DONE,0);
        SubTask subTask2 = new SubTask("К третьей цели","Дополни выполнение",NEW,0);

        taskManager.addSubtasks(subTask);
        taskManager.addSubtasks(subTask1);
        taskManager.addSubtasks(subTask2);

        Task task2 = new Task("Выгулять собаку","Помыть лапы",DONE);
        taskManager.updatingTask(1,task2); //Обновление Таски
        taskManager.setStatusEpic(0, epic); // Изменение статуса Эпика
        taskManager.getSubTaskListId(0); // Получение списка сабтасков определенного Эпика
        taskManager.deleteIdTask(0); // Удаление Задачи/ТАСКИ по ID
        taskManager.deleteIdEpic(0); // Удаление Эпика по ID
        taskManager.getTaskByID(1);  // Получение Таски по ID

        printManager.printTask(taskManager.getTasks()); // Печать таски
        printManager.printEpic(taskManager.getEpic()); // Печать эпик
        printManager.printSubtask(taskManager.getSubTaskList()); // Печать сабтаска


    }
}
