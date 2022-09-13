package manager;

import task.*;
import task.Epic;

import java.io.*;
import java.util.*;

import task.TaskType;
import java.io.BufferedReader;
import java.io.FileReader;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void addTasks(Task task)  { // сохранение создания Task
        super.addTasks(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) { // сохранение создания Epic
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtasks(SubTask subTask) { // сохранение создания Subtask
        super.addSubtasks(subTask);
        save();
    }

    @Override
    public List<Task> getHistory() { // Получение списка  истории Задач
        save();
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void setStatusTask(int taskId, Task task) {
        super.setStatusTask(taskId, task);
        save();
    }

    @Override
    public void updatingTask(int taskId, Task task) {
        super.updatingTask(taskId, task);
        save();
    }

    @Override
    public void updateEpic(int epicId, Epic epic) {
        super.updateEpic(epicId, epic);
    }

    @Override
    public void updateSubTask(int epicId, SubTask subTask) {
        super.updateSubTask(epicId, subTask);
        save();
    }

    @Override
    public void setStatusEpic(int epicId, Epic epic) {
        super.setStatusEpic(epicId, epic);
        save();
    }

    @Override
    public void setStatusSubtask(int epicId, SubTask subtask) {
        super.setStatusSubtask(epicId, subtask);
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteIdTask(int taskId) {
        super.deleteIdTask(taskId);
        save();
    }

    @Override
    public void deleteEpicAndSubtask() {
        super.deleteEpicAndSubtask();
        save();
    }

    @Override
    public void deleteIdEpic(int epicId) {
        super.deleteIdEpic(epicId);
        save();
    }

    @Override
    public void deleteSubTask() {
        super.deleteSubTask();
        save();
    }

    @Override
    public void deleteIdSubtask(int subtaskId) {
        super.deleteIdSubtask(subtaskId);
        save();
    }

    public void save() { // Сохранение Истории вызова задач

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            bufferedWriter.write("id,type,name,status,description,startTime,duration,epicId");
            bufferedWriter.newLine();

            addToString(bufferedWriter, tasks.values());
            addToString(bufferedWriter, epics.values());
            addToString(bufferedWriter, subtasks.values());
            bufferedWriter.newLine();

            addHistoryToString(bufferedWriter, inMemoryHistoryManager.getHistory());
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не считан");
        }
    }

    // Сохранение истории создания TASK
    public static <T extends Task> void addToString(BufferedWriter bufferedWriter, Collection<T> tasks) throws IOException {
        for (Task task : tasks) {
            if (task.getType().equals(TaskType.SUBTASK)) {
                int epicId = ((SubTask) task).getEpicId();
                bufferedWriter.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",task.getId(), task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(),task.getTime(), task.getDuration(),epicId));
            } else {
                bufferedWriter.write((String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(),task.getTime(), task.getDuration())));
            }
        }
    }

    // Сохранение истории работы с TASK, включая удаление, обновление, перезапись дублей
    public static <T extends Task> void addHistoryToString(BufferedWriter bufferedWriter, Collection<T> tasks) throws IOException {
        List<String> historyIds = new ArrayList<>();
        String historyLine;
        for (Task task : tasks) {
            historyIds.add(String.valueOf(task.getId()));
        }
        historyLine = String.join(",", historyIds);
        bufferedWriter.write(historyLine);
    }

    public void loadFromFile(File filename) { // Чтение файла и сохранение данных

        List<Integer> historyNumber;
        Map<Integer, Task> typeTask = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.startsWith("id")) { // Проверка начальной строки

                } else if ((line.contains("TASK") || (line.contains("EPIC")) || (line.contains("SUBTASK")))) { // Проверка искомых значений
                    Task task = fromToString(line);
                    if (task != null) {
                        switch (task.getType()) {
                            case TASK:
                                typeTask.put(task.getId(), task);
                                numberGenerate = task.getId(); // Запись ID в генератор
                                addTasks(task); // Сохранение полученных задач
                                break;
                            case EPIC:
                                typeTask.put(task.getId(), task);
                                numberGenerate = task.getId();
                                addEpic((Epic) task);
                                break;
                            case SUBTASK:
                                typeTask.put(task.getId(), task);
                                numberGenerate = task.getId();
                                addSubtasks((SubTask) task);
                                break;
                        }
                    }
                } else if (line.isEmpty() && line.isBlank()) {

                } else {
                    historyNumber = fromStringList(line);
                    for (Integer i : historyNumber) {
                        Task task = typeTask.get(i);
                        switch (task.getType()) {
                            case TASK:
                                getTaskByID(i);
                                save();
                                break;
                            case EPIC:
                                getEpicByID(i);
                                save();
                                break;
                            case SUBTASK:
                                getIdSubTask(i);
                                save();
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    public static Task fromToString(String line) { // Читаем строки возвращаем значения
        int id = 0;
        String startTime;
        int duration;
        String name;
        String type;
        String description;
        StatusTask status;
        int epicId = 0;

        String[] st = line.split(",");
        if (st[1].equals("TASK") || st[1].equals("EPIC") || st[1].equals("SUBTASK")) { // ПОИСК по статусам
            name = st[2];
            id = Integer.parseInt(st[0]);
            description = st[4];
            status = StatusTask.valueOf(st[3]);
            type = st[1];
            startTime = st[5];
            duration = Integer.parseInt(st[6]);
            switch (type) {
                case "TASK":
                    Task task = new Task(name, description, status,startTime,duration);
                    task.setId(id); // Запись ID в задачу
                    return task;
                case "EPIC":
                    Epic epic = new Epic(name, description, status);
                    epic.setId(id);
                    return epic;
                case "SUBTASK":
                    epicId = Integer.parseInt(st[7]);
                    SubTask subTask = new SubTask(name, description, status,startTime,duration, epicId);
                    subTask.setId(id);
                    return subTask;
            }
        }
        return null;
    }

    public static List<Integer> fromStringList(String line) { // Возращаем историю просмотров

        List<Integer> history = new ArrayList<>();

        String[] st = line.split(",");
        for (String i : st) {
            history.add(Integer.parseInt(i));
        }
        return history;
    }

    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefault();

        Task taskId0New = new Task("Task1", " Descriptoin1", StatusTask.NEW, "01.02.2000 08:00", 60);
        Task taskId1New = new Task("Task2", " Descriptoin1", StatusTask.DONE, "01.02.2000 07:00", 60);
        taskManager.addTasks(taskId0New);
        taskManager.addTasks(taskId1New);

        Epic epicId2New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        Epic epicId3New = new Epic("Epic1", "Descriptoin1", StatusTask.NEW);
        taskManager.addEpic(epicId2New);
        taskManager.addEpic(epicId3New);

        SubTask subTaskId4New = new SubTask("SubTask1", "Descriptoin1", StatusTask.NEW, "01.02.2000 12:00", 60, 3);
        SubTask subTaskId5Done = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 10:00", 60, 3);
        SubTask subTaskId6Done = new SubTask("Subtask2", "Descriptoin2", StatusTask.DONE, "01.02.2000 11:00", 60, 3);
        taskManager.addSubtasks(subTaskId4New);
        taskManager.addSubtasks(subTaskId5Done);
        taskManager.addSubtasks(subTaskId6Done);

        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);

        taskManager.getEpicByID(2);
        taskManager.getEpicByID(3);

        taskManager.getIdSubTask(4);
        taskManager.getIdSubTask(5);
        taskManager.getIdSubTask(6);

        System.out.println("Приоритет");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println();

        System.out.print("История: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Файл создан и заполнен");
        System.out.println("_________________________________________________________________________________");

        // Этот блок зпускается для чтении истории из файла.

        File file = new File("src/task.csv");

        taskManager.loadFromFile(file);
        System.out.println("Печатаем списки задач");

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpic());
        System.out.println(taskManager.getSubTaskList());
        System.out.println();

        System.out.println("Печатаем историю");
        System.out.println(taskManager.getHistory());
    }
}