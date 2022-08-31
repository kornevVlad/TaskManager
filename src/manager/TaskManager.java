package manager;

import task.Task;
import task.SubTask;
import task.Epic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import java.util.ArrayList;

public interface TaskManager {

    void addTasks(Task task); // Сохраняем таски

    void addEpic(Epic epic); // Сохраняем Епики

    void addSubtasks(SubTask subtask);// Сохраняем Сабтаски

    ArrayList<Task> getTasks(); // Получение списка всех задач

    void deleteTask();// Очистка списка Task

    Task getTaskByID(int taskId); // Получение задачи по айди // ИСПРАВЛЕНО

    void updatingTask(int taskId, Task task);// Обновление Задачи по ID

    void deleteIdTask(int taskId); // Удаление Задачи по ID

    ArrayList<Epic> getEpic(); // Получение списка всех Эпиков // ИСПРАВЛЕНО

    Epic getEpicByID(int epicId); // Получение Эпика по ID // ИСПРАВЛЕНО

    void deleteEpicAndSubtask(); // Очистка списка Epic,SubTask

    void updateEpic(int epicId, Epic epic); //Обновление Эпика

    void deleteIdEpic(int epicId); // Удаление Эпика по ID

    ArrayList<SubTask> getSubTaskList(); // Получение списка всех Сабтасков

    void deleteSubTask(); // Очистка списка SubTask

    SubTask getIdSubTask(int epicId); // Получение Сабтаска по ID

    void updateSubTask(int epicId, SubTask subTask); //Обновление Сабтаска

    ArrayList<SubTask> getSubTaskListId(int epicId); // Получение списка всех подзадач определённого эпика.

    void setStatusTask(int taskId, Task task); // Обновление статуса задачи // ИСПРАВЛЕНО

    void setStatusEpic(int epicId, Epic epic); // Обновление Статуса Эпика

    void setStatusSubtask(int epicId, SubTask subtask); // Обновление статуса Сабтаски

    List<Task> getHistory(); // История просмотренных задач

    void loadFromFile(File file) throws IOException;
}