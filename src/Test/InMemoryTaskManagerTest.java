package Test;

import manager.InMemoryTaskManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createManager() {
        taskManager = new InMemoryTaskManager();
        super.saveTask();
    }

    @AfterEach
    public void clearManager(){
        clearTaskManager();
    }

    @Test
    @DisplayName("Сохранение и удаление Task")
    public void addTasks(){
        List<Task> task = taskManager.getTasks();
        assertEquals(task.size(),2);

        taskManager.deleteTask(); // Очистка списка
        List<Task> testTask = taskManager.getTasks();
        List<Task> testList = new ArrayList<>();
        assertEquals(testTask.size(), testList.size());

        Task task2 = new Task("Task2"," Descriptoin1", StatusTask.DONE,"01.02.2000 08:00",60);
        taskManager.addTasks(task2);
        testTask=taskManager.getTasks();
        assertEquals(testTask.size(), 1);
    }

    @Test
    @DisplayName("Сохранение Epic")
    public void addEpic(){
        List<Epic> epic = taskManager.getEpic();
        assertEquals(epic.size(),2);

        taskManager.deleteEpicAndSubtask();
        Epic epic2 = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        taskManager.addEpic(epic2);
        List<Epic> testEpic =taskManager.getEpic();
        assertEquals(testEpic.size(), 1);
    }

    @Test
    @DisplayName("Сохранение и удаление Subtask")
    public void addSubtasks(){
        List<SubTask> subTasks = taskManager.getSubTaskList();
        assertEquals(subTasks.size(),3);

        taskManager.deleteSubTask(); // Очистка списка
        SubTask subTask2 = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 10:00", 60, 3);
        taskManager.addSubtasks(subTask2);
        List<SubTask> testSubtask =taskManager.getSubTaskList();
        assertEquals(testSubtask.size(), 1);
    }

    @Test
    @DisplayName("Обновление по ID Task")
    public  void updatingTask(){
        Task task =  taskManager.getTaskByID(0);
        assertEquals(task.getStatus(),StatusTask.NEW);

        task.setStatus(StatusTask.DONE);
        taskManager.updatingTask(0,task);
        assertEquals(task.getStatus(),StatusTask.DONE);
    }

    @Test
    @DisplayName("Обновление по ID Epic")
    public void updatingEpic(){
        Epic epic = taskManager.getEpicByID(2);
        assertEquals(epic.getStatus(),StatusTask.NEW);

        epic.setStatus(StatusTask.DONE);
        taskManager.updateEpic(2,epic);
        assertEquals(epic.getStatus(),StatusTask.DONE);
    }

    @Test
    @DisplayName("Обновление по ID SubTask")
    public void updatingSubTask(){
        SubTask subTask = taskManager.getIdSubTask(4);
        assertEquals(subTask.getStatus(),StatusTask.NEW);

        subTask.setStatus(StatusTask.DONE);
        taskManager.updateSubTask(4,subTask);
        assertEquals(subTask.getStatus(),StatusTask.DONE);
    }

    @Test
    @DisplayName("Удаление/Получение по ID TASK")
    public void deleteIdTask(){
        assertNotNull(taskManager.getTaskByID(0));
        taskManager.deleteIdTask(0);
        assertNull(taskManager.getTaskByID(0));
    }

    @Test
    @DisplayName("Удаление/Получение по ID Epic")
    public void deleteIdEpic(){
        assertNotNull(taskManager.getEpicByID(2));
        taskManager.deleteIdEpic(2);
        assertNull(taskManager.getEpicByID(2));
    }

    @Test
    @DisplayName("Удаление/Получение по ID Subtask")
    public void deleteIdSubtask(){
        assertNotNull(taskManager.getIdSubTask(5));
        taskManager.deleteIdSubtask(5);
        assertNull(taskManager.getIdSubTask(5));
    }

    @Test
    @DisplayName("Очистка Epic и с ним его Subtask")
    public void deleteEpicAndSubtask(){
        assertNotNull(taskManager.getEpic());
        assertNotNull(taskManager.getSubTaskList());

        taskManager.deleteEpicAndSubtask();
        List<SubTask> testSubtask =taskManager.getSubTaskList();
        List<SubTask> testList = new ArrayList<>();
        assertEquals(testSubtask.size(), testList.size());
    }

    @Test
    @DisplayName("Изменение статуса Epic")
    public void updateStatusEpic(){
        Epic epic = taskManager.getEpicByID(3);
        assertEquals(epic.getStatus(),StatusTask.NEW);

        SubTask subTask = taskManager.getIdSubTask(4);
        subTask.setStatus(StatusTask.DONE);
        taskManager.setStatusSubtask(subTask.getId(),subTask);
        taskManager.setStatusEpic(epic.getId(),epic);
        Epic epic1 = taskManager.getEpicByID(3);

        assertEquals(epic1.getStatus(),StatusTask.DONE);
    }
}