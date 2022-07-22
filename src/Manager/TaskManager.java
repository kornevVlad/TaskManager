package Manager;

import Task.Task;
import Task.SubTask;
import Task.Epic;
import Task.StatusTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private   Map<Integer, Task> tasks = new HashMap<>();
    private   Map<Integer, SubTask> subtasks = new HashMap<>();
    private   Map<Integer, Epic> epics = new HashMap<>();

    private int numberGenerateTask = 0;
    private int numberGenerateEpic = 0;
    private int numberGenerateSubTask = 0;


    public void addTasks(Task task) { // Сохраняем таски
        int taskId = numberGenerateTask++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void addEpic(Epic epic) { // Сохраняем Епики
        int epicId = numberGenerateEpic++;
        epic.setId(epicId);
        this.epics.put(epicId, epic);
    }

    public void addSubtasks(SubTask subtask) { // Сохраняем Сабтаски
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);// Проверяем есть ли Эпик данной сабтаски
        if (epic == null) {
            return;
        }
        int subTaskId = numberGenerateSubTask++;
        subtask.setId(subTaskId);
        this.subtasks.put(subTaskId, subtask);
        epic.addSubtask(subTaskId);
    }

    public ArrayList<Task> getTasks() { // Получение списка всех задач
        Collection<Task> taskStorage = tasks.values();
        return new ArrayList<>(taskStorage);
    }

    public void deleteTask() { // Очистка списка Task
        tasks.clear();
    }

    public Task getTaskByID(int taskId) { // Получение задачи по айди // ИСПРАВЛЕНО
        return tasks.get(taskId);
    }

    public void updatingTask(int taskId, Task task) {// Обновление Задачи по ID
        if (tasks.containsKey(taskId)) {
            Task getTask = tasks.get(taskId);
            if (taskId == getTask.getId()) {
                task.setId(taskId);
                tasks.put(taskId, task);
            }
        }
    }

    public Integer deleteIdTask(int taskId) { // Удаление Задачи по ID
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
        return taskId;
    }

    public ArrayList<Epic> getEpic() { // Получение списка всех Эпиков // ИСПРАВЛЕНО
        return new ArrayList<>(epics.values());
    }

    public Epic getEpicByID(int epicId) { // Получение Эпика по ID // ИСПРАВЛЕНО
        return epics.get(epicId);
    }

    public void deleteEpicAndSubtask() { // Очистка списка Epic,SubTask
        epics.clear();
        subtasks.clear();
    }

    public void updateEpic(int epicId, Epic epic) { //Обновление Эпика
        if (epics.containsKey(epicId)) {
            epic.setId(epicId);
            epics.put(epicId, epic);
        }
    }

    public void deleteIdEpic(int epicId) { // Удаление Эпика по ID
        epics.remove(epicId);
    }

    public ArrayList<SubTask> getSubTaskList() { // Получение списка всех Сабтасков
        Collection<SubTask> subtaskStorage = subtasks.values();
        return new ArrayList<>(subtaskStorage);
    }


    public void deleteSubTask() { // Очистка списка SubTask
        subtasks.clear();
    }

    public SubTask getIdSubTask(int epicId) { // Получение Сабтаска по ID
        return subtasks.get(epicId);
    }

    public void updateSubTask(int epicId, SubTask subTask) { //Обновление Сабтаска
        if (subtasks.containsKey(epicId)) {
            subTask.setId(epicId);
            subtasks.put(epicId, subTask);
        }
    }


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

    public void setStatusTask(int taskId, Task task) { // Обновление статуса задачи // ИСПРАВЛЕНО
        Task taskValue = tasks.get(taskId);
        if (taskValue.getStatus().equals(StatusTask.IN_PROGRES)) {
            tasks.put(taskId, task);
        } else if (taskValue.getStatus().equals(StatusTask.DONE)) {
            tasks.put(taskId, task);
        }
    }

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

    public void setStatusSubtask(int epicId, SubTask subtask){ // Обновление статуса Сабтаски
        if(subtask.getStatus().equals(StatusTask.IN_PROGRES)){
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        } else if(subtask.getStatus().equals(StatusTask.DONE)){
            subtask.setId(epicId);
            subtasks.put(epicId, subtask);
        }
    }
}