package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer  {

    private final TaskManager taskManager;
    private static final int PORT = 8079;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    protected final HttpServer httpServer;

    public HttpTaskServer() throws IOException {

        taskManager =   Managers.getDefault();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void start() {
        httpServer.start();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            int id;
            String method = httpExchange.getRequestMethod();

            try {
            String path = httpExchange.getRequestURI().getPath();
                String[] splitStrings = path.split("/");
                String query = httpExchange.getRequestURI().getRawQuery();
                String[] splitId = new String[]{""};

                if (query != null) {
                    splitId = query.split("=");
                }

                switch (method) {
                    case "GET": // ВЫВОД ДАННЫХ

                        if (splitStrings[splitStrings.length - 1].equals("task") && (query == null)) { // Список TASk
                            String taskJson = gson.toJson(taskManager.getTasks());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(taskJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query == null)) { // Список EPIC
                            String epicJson = gson.toJson(taskManager.getEpic());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(epicJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query == null)) { // Список SUBTASK
                            String subtaskJson = gson.toJson(taskManager.getSubTaskList());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(subtaskJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("task") && (query != null)) { // ID TASK
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String taskIdJson = gson.toJson(taskManager.getTaskByID(id));
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(taskIdJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query != null)) { // ID EPIC
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String epicIdJson = gson.toJson(taskManager.getEpicByID(id));
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(epicIdJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query != null)) { // ID SUBTASK
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String subtaskIdJson = gson.toJson(taskManager.getIdSubTask(id));
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(subtaskIdJson.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("history")) { // Список ИСТОРИИ(TASK,EPIC,SUBTASK)
                            String history = gson.toJson(taskManager.getHistory());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(history.getBytes());
                            }
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("priority")) { // Список приоритетов
                            String history = gson.toJson(taskManager.getPrioritizedTasks());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(history.getBytes());
                            }
                            break;
                        }


                    case "POST":  // ДОБАВЛЕНИЕ, ИЗМЕНЕНИЕ ДАННЫХ

                        if (splitStrings[splitStrings.length - 1].equals("task") && (query == null)) { // Добавление TASK
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newTask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Task task = gson.fromJson(newTask, Task.class);
                            taskManager.addTasks(task);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        else if (splitStrings[splitStrings.length - 1].equals("task") && (query != null)) { //Изменение TASK ID
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newTask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Task task = gson.fromJson(newTask, Task.class);
                            taskManager.updatingTask(id, task);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        else if (splitStrings[splitStrings.length - 1].equals("epic") && (query == null)) { // Добавление EPIC
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newEpic = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Epic epic = gson.fromJson(newEpic, Epic.class);
                            taskManager.addEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        else if (splitStrings[splitStrings.length - 1].equals("epic") && (query != null)) { // Изменение EPIC
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newEpic = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Epic epic = gson.fromJson(newEpic, Epic.class);
                            taskManager.updatingTask(id, epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        else if (splitStrings[splitStrings.length - 1].equals("subtask") && (query == null)) { // Добавление SUBTASK
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newSubtask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            SubTask subTask = gson.fromJson(newSubtask, SubTask.class);
                            taskManager.addSubtasks(subTask);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        else if (splitStrings[splitStrings.length - 1].equals("subtask") && (query != null)) { // Добавление SUBTASK
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newSubtask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            SubTask subTask = gson.fromJson(newSubtask, SubTask.class);
                            taskManager.updateSubTask(id, subTask);
                            httpExchange.sendResponseHeaders(201, 0);

                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                        }
                        break;


                    case "DELETE": // УДАЛЕНИЕ ДАННЫХ

                        if (splitStrings[splitStrings.length - 1].equals("task") && (query == null)) { // Очистка Списка TASK
                            taskManager.deleteTask();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("task") && (query != null)) { // Удаление ID TASK
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            taskManager.deleteIdTask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query == null)) { // Очистка списка EPIC, SUBTASK
                            taskManager.deleteEpicAndSubtask();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query != null)) { // Удаление ID EPIC
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            taskManager.deleteIdEpic(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query == null)) { // Очистка списка SUBTASK
                            taskManager.deleteSubTask();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query != null)) { // Очистка списка SUBTASK
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            taskManager.deleteIdSubtask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                }
                httpExchange.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}