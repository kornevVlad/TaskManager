package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.StatusTask;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();

    protected TreeSet<Task> sortedList = new TreeSet<>(Comparator.comparing(Task::getStartTime)); // хранение задач

    protected InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    protected int numberGenerate = 0;

    @Override
    public void addTasks(Task task)  { // Сохраняем таски

        if(validationTask(task)) { // Проверка валидации
            int taskId = numberGenerate++;
            task.setId(taskId);
            tasks.put(taskId, task);
            sortedList.add(task);
        }else {
            return;
        }
    }

    @Override
    public void addEpic(Epic epic) { // Сохраняем Епики
        int epicId = numberGenerate++;
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    @Override
    public void addSubtasks(SubTask subtask) { // Сохраняем Сабтаски

        if(validationTask(subtask)) { // Проверка валидации
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);// Проверяем есть ли Эпик данной сабтаски
            if (epic == null) {
                return;
            }
            int subtaskId = numberGenerate++;
            subtask.setId(subtaskId);
            this.subtasks.put(subtaskId, subtask);
            epic.addSubtask(subtaskId); // Присваивание Сабтаска Эпику
            strartDataTimeEpic(subtask);
            endDataTimeEpic(subtask);
            sortedList.add(subtask);
        }else {
            return;
        }
    }

    @Override
    public ArrayList<Task> getTasks() { // Получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTask() { // Очистка списка Task
        for (Task task : tasks.values()){
            sortedList.remove(task);
            inMemoryHistoryManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public Task getTaskByID(int taskId) { // Получение задачи по айди // ИСПРАВЛЕНО
        inMemoryHistoryManager.addHistory(tasks.get(taskId)); // Запись Таск в Историю
        if(tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }else {
            return null;
        }
    }

    @Override
    public void updatingTask(int taskId, Task task) {// Обновление Задачи по ID
        if (tasks.containsKey(taskId)) {
            Task getTask = tasks.get(taskId);
            if (taskId == getTask.getId()) {
                task.setId(taskId);
                tasks.put(taskId, task);
            }
        }
    }

    @Override
    public void deleteIdTask(int taskId) { // Удаление Задачи по ID
        Task task = tasks.remove(taskId);
        inMemoryHistoryManager.remove(taskId); // Удаление Задачи из Истории просмотров
        sortedList.remove(task);
    }

    @Override
    public ArrayList<Epic> getEpic() { // Получение списка всех Эпиков // ИСПРАВЛЕНО
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicByID(int epicId) { // Получение Эпика по ID // ИСПРАВЛЕНО
        if (epics.containsKey(epicId)) {
            inMemoryHistoryManager.addHistory(epics.get(epicId)); // Запись Эпик в Историю
            return epics.get(epicId);
        }
        return null;
    }

    @Override
    public void deleteEpicAndSubtask() { // Очистка списка Epic,SubTask
    for(Epic epic :epics.values()){
        inMemoryHistoryManager.remove(epic.getId());
        }
        epics.clear();
    for (SubTask subTask: subtasks.values()){
        inMemoryHistoryManager.remove(subTask.getId());
        sortedList.remove(subTask);
    }
        subtasks.clear();
    }

    @Override
    public void updateEpic(int epicId, Epic epic) { //Обновление Эпика
        if (epics.containsKey(epicId)) {
            epic.setId(epicId);
            epics.put(epicId, epic);
        }
    }

    @Override
    public void deleteIdEpic(int epicId) { // Удаление Эпика по ID и с ним его Сабтаск
        List<SubTask> subs = getSubTaskListId(epicId);
        for (SubTask sub : subs) {
            int i = sub.getEpicId();
            if (i == epicId) {
                int j = sub.getId();
                subtasks.remove(j);
                inMemoryHistoryManager.remove(j);
            }
        }
        inMemoryHistoryManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() { // Получение списка всех Сабтасков
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubTask() { // Очистка списка SubTask
        for (SubTask subTask :subtasks.values() ) {
            sortedList.remove(subTask);
            inMemoryHistoryManager.remove(subTask.getId());
        }
        subtasks.clear();
    }

    @Override
    public void deleteIdSubtask(int subtaskId) { // Удаление Сабтаска по ID
        if (subtasks.containsKey(subtaskId)) {
            SubTask subTask =  subtasks.get(subtaskId);
            deleteTimeEpic(subtasks.get(subtaskId));
            inMemoryHistoryManager.remove(subtaskId);
           subtasks.remove(subtaskId);
           sortedList.remove(subTask);
        }
    }

    @Override
    public SubTask getIdSubTask(int subtaskId) { // Получение Сабтаска по ID //Исправлено
        inMemoryHistoryManager.addHistory(subtasks.get(subtaskId)); // Запись Сабтаска в Историю
        return subtasks.get(subtaskId);
    }

    @Override
    public void updateSubTask(int epicId, SubTask subTask) { //Обновление Сабтаска
        if (subtasks.containsKey(epicId)) {
            subTask.setId(epicId);
            subtasks.put(epicId, subTask);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTaskListId(int epicId) { // Получение списка всех подзадач определённого эпика.
        Collection<SubTask> subtaskStorage = subtasks.values();
        ArrayList<SubTask> subTasksId = new ArrayList<>();
        for (SubTask valueSubtask : subtaskStorage) {
            if (valueSubtask.getEpicId() == epicId) {
                subTasksId.add(valueSubtask);
            }
        }
        return subTasksId;
    }

    @Override
    public void setStatusTask(int taskId, Task task) { // Обновление статуса задачи // ИСПРАВЛЕНО
        Task taskValue = tasks.get(taskId);
        if (taskValue.getStatus().equals(StatusTask.IN_PROGRES)) {
            tasks.put(taskId, task);
        } else if (taskValue.getStatus().equals(StatusTask.DONE)) {
            tasks.put(taskId, task);
        }
    }

    @Override
    public void setStatusEpic(int epicId, Epic epic) { // Обновление Статуса Эпика
        Epic value = epics.get(epicId);
        if (value.getSubtaskId().size() > 0) {
            Collection<SubTask> subtask = subtasks.values();

            int isNew = 0;
            int isProgres = 0;
            int isDone = 0;
            for (SubTask sub : subtask) {
                if (sub.getStatus().equals(StatusTask.NEW) && (sub.getEpicId() == epicId)) {
                    isNew++;
                } else if (sub.getStatus().equals(StatusTask.IN_PROGRES) && (sub.getEpicId() == epicId)) {
                    isProgres++;
                } else if (sub.getStatus().equals(StatusTask.DONE) && (sub.getEpicId() == epicId)) {
                    isDone++;
                }
            }
            if ((isDone <= 0) && (isProgres <= 0)) {
                epic.setStatus(StatusTask.NEW);
                epic.setId(epicId);
                epics.put(epicId, epic);
            } else if ((isNew <= 0) && (isProgres <= 0)) {
                epic.setStatus(StatusTask.DONE);
                epic.setId(epicId);
                epics.put(epicId, epic);
            } else {
                epic.setStatus(StatusTask.IN_PROGRES);
                epic.setId(epicId);
                epics.put(epicId, epic);
            }

        } else { // Если Эпик не имеет Сабтасков выполни это действие.
            if (value.getStatus().equals(StatusTask.IN_PROGRES)) {
                epic.setId(epicId);
                epics.put(epicId, epic);
            } else if (value.getStatus().equals(StatusTask.DONE)) {
                epic.setId(epicId);
                epics.put(epicId, epic);
            }
        }
    }

    @Override
    public void setStatusSubtask(int epicId, SubTask subtask) { // Обновление статуса Сабтаски
        if (subtask.getStatus().equals(StatusTask.IN_PROGRES)) {
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        } else if (subtask.getStatus().equals(StatusTask.DONE)) {
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        }
    }

    public List<Task> getHistory() { // Получение списка  истории Задач
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void loadFromFile(File file) throws IOException {
    }

    public void strartDataTimeEpic(SubTask subtask) { // Добавление начального времени в Епик зависимый от Сабтаск
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic.getTime() == null) {
            String start = subtask.getTime();
            epic.setStartTime(start);
        } else if (epic.getTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String startEpic = epic.getTime();
            LocalDateTime startTimeEpic = LocalDateTime.parse(startEpic, formatter);
            String startSubtask = subtask.getTime();
            LocalDateTime startTimeSubtask = LocalDateTime.parse(startSubtask, formatter);
            if (startTimeEpic.isAfter(startTimeSubtask)) {
                epic.setStartTime(startSubtask);
            }
        }
    }

    public void endDataTimeEpic(SubTask subTask) { // Расчет времени окончания Epic при добавлении Subtask
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        int endTime = subTask.getDuration();
        int endTimeEpic = epic.getDuration();
        endTimeEpic = endTimeEpic + endTime;
        epic.setDuration(endTimeEpic);
    }

    public void deleteTimeEpic(SubTask subTask) { // Удаление времени из Epic при удалении Subtask
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        int endTime = subTask.getDuration();
        int endTimeEpic = epic.getDuration();
        endTimeEpic = endTimeEpic - endTime;
        epic.setDuration(endTimeEpic);
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {  // Сортировка по приоритету
        ArrayList<Task> priority = new ArrayList<>();
        for(Task task : sortedList) {
            priority.add(task);
        }
        return priority;
    }

    public  boolean validationTask(Task task) { // Проверка на валидацию

        if (task == null) {
            return false;
        }
            LocalDateTime startlValid, endValid, newTaskStart, newTaskEnd;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            for (Task valid : sortedList) {
                startlValid = LocalDateTime.parse(valid.getStartTime(),formatter); // Стартовое время
                endValid = LocalDateTime.parse(valid.getEndTime(),formatter); // Конечное время
                newTaskStart = LocalDateTime.parse(task.getStartTime(),formatter); // Стартовое время входящей задачи
                newTaskEnd = LocalDateTime.parse(task.getEndTime(),formatter);  // Конечное время входящей задачи

                boolean first = startlValid.isBefore(newTaskStart); // стартовое время до входящего стартового
                boolean second = endValid.isAfter(newTaskStart); // конечное время после входящего стартового
                boolean third = startlValid.isAfter(newTaskStart); // стартовое время после входящего стартового
                boolean fourth = startlValid.isBefore(newTaskEnd); // стартовое время до входящего конечного
                boolean equally = startlValid.equals(newTaskStart); // стартовое время равно входящему

                if(equally){
                    return false;
                }
                if (first && second) {
                    return false;
                }
                if (third && fourth){
                    return false;
                }
        }
            return true;
    }

    @Override
    public void save() {

    }

    @Override
    public boolean load() {

        return false;
    }
}