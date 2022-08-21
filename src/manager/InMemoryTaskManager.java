package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.StatusTask;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private   Map<Integer, Task> tasks = new HashMap<>();
    private   Map<Integer, SubTask> subtasks = new HashMap<>();
    private   Map<Integer, Epic> epics = new HashMap<>();

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    private int numberGenerate = 0;

    @Override
    public void addTasks(Task task) { // Сохраняем таски
        int taskId = numberGenerate++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void addEpic(Epic epic) { // Сохраняем Епики
        int epicId = numberGenerate++;
        epic.setId(epicId);
        this.epics.put(epicId, epic);
    }

    @Override
    public void addSubtasks(SubTask subtask) { // Сохраняем Сабтаски
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);// Проверяем есть ли Эпик данной сабтаски
        if (epic == null) {
            return;
        }
        int subTaskId = numberGenerate++;
        subtask.setId(subTaskId);
        this.subtasks.put(subTaskId, subtask);
        epic.addSubtask(subTaskId);
    }

    @Override
    public ArrayList<Task> getTasks() { // Получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTask() { // Очистка списка Task
        tasks.clear();
    }

    @Override
    public Task getTaskByID(int taskId) { // Получение задачи по айди // ИСПРАВЛЕНО
        inMemoryHistoryManager.addHistory(tasks.get(taskId)); // Запись Таск в Историю
        return tasks.get(taskId);
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
            tasks.remove(taskId);
            inMemoryHistoryManager.remove(taskId); // Удаление Задачи из Истории просмотров
    }

    @Override
    public ArrayList<Epic> getEpic() { // Получение списка всех Эпиков // ИСПРАВЛЕНО
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicByID(int epicId) { // Получение Эпика по ID // ИСПРАВЛЕНО
        if(epics.containsKey(epicId)) {
            inMemoryHistoryManager.addHistory(epics.get(epicId)); // Запись Эпик в Историю
            return epics.get(epicId);
        }
        return null;
    }

    @Override
    public void deleteEpicAndSubtask() { // Очистка списка Epic,SubTask
        epics.clear();
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
           int i =  sub.getEpicId();
           if(i == epicId) {
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
        subtasks.clear();
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
            for(SubTask sub : subtask){
                if(sub.getStatus().equals(StatusTask.NEW) && (sub.getEpicId() == epicId)){
                    isNew++;
                }else if (sub.getStatus().equals(StatusTask.IN_PROGRES) && (sub.getEpicId() == epicId)){
                    isProgres++;
                }else if (sub.getStatus().equals(StatusTask.DONE) && (sub.getEpicId() == epicId)){
                    isDone++;
                }
            }
            if((isDone <= 0) && (isProgres <= 0)){
                epic.setStatus(StatusTask.NEW);
                epic.setId(epicId);
                epics.put(epicId, epic);
            } else if ((isNew <= 0) && (isProgres <= 0)){
                epic.setStatus(StatusTask.DONE);
                epic.setId(epicId);
                epics.put(epicId, epic);
            }else {
                epic.setStatus(StatusTask.DONE);
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
    public void setStatusSubtask(int epicId, SubTask subtask){ // Обновление статуса Сабтаски
        if(subtask.getStatus().equals(StatusTask.IN_PROGRES)){
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        } else if(subtask.getStatus().equals(StatusTask.DONE)){
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        }
    }

    public List<Task> getHistory(){ // Получение списка  истории Задач
       return inMemoryHistoryManager.getHistory();
    }
}