package manager;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import httpclient.KVTaskClient;

import task.Epic;
import task.Task;
import task.SubTask;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager{

    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private final KVTaskClient kvTaskClient = new KVTaskClient();
    private final Gson gson = Managers.getGson();

    public HttpTaskManager() throws IOException {
        super(null);
    }

    @Override
    public void save(){ //Сохранение на сервер

        String jsonTask = gson.toJson(tasks);
        kvTaskClient.save("tasks",jsonTask);

        String jsonEpics = gson.toJson(epics);
        kvTaskClient.save("epics",jsonEpics);

        String jsonSubtask = gson.toJson(subtasks);
        kvTaskClient.save("subtasks",jsonSubtask);


        List<Task> history =  getHistory();
        String jsonHistory = gson.toJson(history);
        kvTaskClient.save("history", jsonHistory);
    }


    @Override
    public boolean load() { // Загрузка с сервера

        String tasksJson = kvTaskClient.load("tasks");
        String epicsJson = kvTaskClient.load("epics");
        String subtasksJson = kvTaskClient.load("subtasks");
        String historyJson = kvTaskClient.load("history");

        tasks = gson.fromJson(tasksJson, new TypeToken<HashMap<Integer, Task>>(){}.getType());
        epics= gson.fromJson(epicsJson, new TypeToken<HashMap<Integer, Epic>>(){}.getType());
        subtasks = gson.fromJson(subtasksJson, new TypeToken<HashMap<Integer, SubTask>>(){}.getType());
        ArrayList<Task> tasksHistoryJson = gson.fromJson(historyJson, new TypeToken<ArrayList<Task>>(){}.getType());

        for (Task taskhistory : tasksHistoryJson){ // Присваивание значения TASK, EPIC, SUBTASK
         Task task = tasks.get(taskhistory.getId());
         Epic epic = epics.get(taskhistory.getId());
         SubTask subTask = subtasks.get(taskhistory.getId());
            if(task != null){
                inMemoryHistoryManager.addHistory(task);
            }else if(epic != null){
                inMemoryHistoryManager.addHistory(epic);
            }else {
                inMemoryHistoryManager.addHistory(subTask);
            }
        }
        return false;
    }
}