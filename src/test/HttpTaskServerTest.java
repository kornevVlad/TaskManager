package test;

import com.google.gson.Gson;
import httpclient.KVTaskClient;

import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import server.KVServer;

import org.junit.jupiter.api.*;

import task.Epic;
import task.StatusTask;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client;
    KVTaskClient kvTaskClient;
    Gson gson = new Gson();

    @BeforeEach
    public void createManger() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
            kvTaskClient = new KVTaskClient();
            client = HttpClient.newHttpClient();
            valueTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Тестовые данные")
    public void valueTest() {
        Task task = new Task("Task1", " Descriptoin1", StatusTask.NEW, "01.02.2000 07:00", 60);
        httpTaskServer.getTaskManager().addTasks(task);
        Epic epic = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        httpTaskServer.getTaskManager().addEpic(epic);
        SubTask subTask = new SubTask("Subtask3", "Descriptoin2", StatusTask.NEW, "01.02.2000 11:00", 60, 1);
        httpTaskServer.getTaskManager().addSubtasks(subTask);
    }


    @Test
    @DisplayName("Создание TASK")
    public void createTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/task/");

        Task newTask = new Task("TaskNew", " DescriptoinNew", StatusTask.NEW, "01.02.2000 18:00", 60);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> task = httpTaskServer.getTaskManager().getTasks();
        assertEquals(task.size(), 2);
    }

    @Test
    @DisplayName("Создание EPIC")
    public void createEpic() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8079/tasks/epic/");

        Epic newEpic = new Epic("newEpic", "newDescriptoin", StatusTask.NEW);
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epics = httpTaskServer.getTaskManager().getEpic();
        assertEquals(epics.size(), 2);
    }

    @Test
    @DisplayName("Создание SubTask")
    public void createSubTask() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8079/tasks/subtask/");

        SubTask newSubTask = new SubTask("newSubtask", "Descriptoin2", StatusTask.NEW, "01.02.2000 13:00", 60, 1);
        String json = gson.toJson(newSubTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasks = httpTaskServer.getTaskManager().getSubTaskList();
        assertEquals(subTasks.size(), 2);
    }

    @Test
    @DisplayName("Получение TASK ID")
    public void getTaskId() throws IOException, InterruptedException {
        String taskJson = gson.toJson(httpTaskServer.getTaskManager().getTaskByID(0));

        URI url = URI.create("http://localhost:8079/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), taskJson);
    }

    @Test
    @DisplayName("Получение Epic ID")
    public void getEpicId() throws IOException, InterruptedException {
        String epicJson = gson.toJson(httpTaskServer.getTaskManager().getEpicByID(1));

        URI url = URI.create("http://localhost:8079/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), epicJson);
    }

    @Test
    @DisplayName("Получение SubTask ID")
    public void getSubTaskId() throws IOException, InterruptedException {
        String subTaskJson = gson.toJson(httpTaskServer.getTaskManager().getIdSubTask(2));

        URI url = URI.create("http://localhost:8079/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), subTaskJson);
    }

    @Test
    @DisplayName("Удаление TASK")
    public void deleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> task = httpTaskServer.getTaskManager().getTasks();
        assertEquals(task.size(), 0);
    }

    @Test
    @DisplayName("Удаление EPIC")
    public void deleteEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epics = httpTaskServer.getTaskManager().getEpic();
        assertEquals(epics.size(), 0);
    }

    @Test
    @DisplayName("Удаление SUBTASK")
    public void deleteSubTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasks = httpTaskServer.getTaskManager().getSubTaskList();
        assertEquals(subTasks.size(), 0);
    }

    @Test
    @DisplayName("Удаление TASK ID")
    public void deleteTaskID() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "");
    }

    @Test
    @DisplayName("Удаление EPIC ID")
    public void deleteEpicID() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "");
    }

    @Test
    @DisplayName("Удаление SubTask ID")
    public void deleteSubTaskID() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "");
    }

    @Test
    @DisplayName("Получение истории")
    public void getHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8079/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(),"[]");

         httpTaskServer.getTaskManager().getTaskByID(0);
         httpTaskServer.getTaskManager().getEpicByID(1);
         httpTaskServer.getTaskManager().getIdSubTask(2);

        String str = gson.toJson(httpTaskServer.getTaskManager().getHistory());
        HttpRequest newRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> newResponse = client.send(newRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(newResponse.body(),str);
    }
}

