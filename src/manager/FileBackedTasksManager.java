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
    public void addTasks(Task task) { // сохранение создания Task
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

    public void save() { // Сохранение Истории вызова задач

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            bufferedWriter.write("id,type,name,status,description,epic");
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
                bufferedWriter.write(String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(), epicId));
            } else {
                bufferedWriter.write((String.format("%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getName(),
                        task.getStatus(), task.getDescription())));
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
            switch (type) {
                case "TASK":
                    Task task = new Task(name, description, status);
                    task.setId(id); // Запись ID в задачу
                    return task;
                case "EPIC":
                    Epic epic = new Epic(name, description, status);
                    epic.setId(id);
                    return epic;
                case "SUBTASK":
                    epicId = Integer.parseInt(st[5]);
                    SubTask subTask = new SubTask(name, description, status, epicId);
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

        Task task = new Task("Task1"," Descriptoin1", StatusTask.NEW);
        Task task1 = new Task("Task2","Descriptoin2",StatusTask.NEW);
        Task task2 = new Task("Task3","Descriptoin3",StatusTask.NEW);

        taskManager.addTasks(task);
        taskManager.addTasks(task1);
        taskManager.addTasks(task2);

        //Создание и сохранение эпика
        Epic epic = new Epic( "Epic1","Descriptoin1",StatusTask.NEW);
        Epic epic1 = new Epic( "Еpic2","Descriptoin2",StatusTask.NEW );

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);

        //Создание и сохранение сабтаски
        SubTask subTask = new SubTask("SubTask1","Descriptoin1",StatusTask.DONE,3);
        SubTask subTask1 = new SubTask("Subtask2","Descriptoin2",StatusTask.DONE,3);
        SubTask subTask2 = new SubTask("Subtask3","Descriptoin3",StatusTask.DONE,3);

        taskManager.addSubtasks(subTask);
        taskManager.addSubtasks(subTask1);
        taskManager.addSubtasks(subTask2);

        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(0);

        taskManager.getEpicByID(3);
        taskManager.getEpicByID(4);

        taskManager.getIdSubTask(5);
        taskManager.getIdSubTask(6);
        taskManager.getIdSubTask(7);

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
        System.out.println("Печатаем историю");
        System.out.println(taskManager.getHistory());
    }
}