package test;

import manager.HttpTaskManager;

import org.junit.jupiter.api.*;

import server.KVServer;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>  {

    KVServer kvServer;

    @BeforeEach
    public void createManger()  {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = new HttpTaskManager();
            super.saveTask();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Проверка сохранения данных на сервере")
    public void saveKVServer(){
        int numberEndpointsOnServer = 4;
        List<String> newData = new ArrayList<>();
        newData.addAll(kvServer.getData().values());
        assertEquals(newData.size(), numberEndpointsOnServer);
    }

    @Test
    @DisplayName("Выгрузка данных сервера")
    public void loadKVServer(){
        List<Task> tasksBeforeLoad = taskManager.getTasks();
        List<Epic> epicsBeforeLoad = taskManager.getEpic();
        List<SubTask> subTasksBeforeLoad = taskManager.getSubTaskList();

        taskManager.load(); // заргрузка данных из сервера

        List<Task> tasksAfterLoad = taskManager.getTasks();
        List<Epic> epicsAfterLoad = taskManager.getEpic();
        List<SubTask> subTasksAfterLoad = taskManager.getSubTaskList();


        assertEquals(tasksBeforeLoad.size(),tasksAfterLoad.size());
        assertEquals(epicsBeforeLoad.size(), epicsAfterLoad.size());
        assertEquals(subTasksBeforeLoad.size(), subTasksAfterLoad.size());
    }

    @Test
    @DisplayName("Очистка данных на сервере")
    public void clearKVServer(){
        taskManager.deleteTask();
        taskManager.deleteEpicAndSubtask();

        List<String> newData = new ArrayList<>();
        newData.addAll(kvServer.getData().values());
        String str = newData.get(0);

        assertEquals(str,"{}");
    }
}