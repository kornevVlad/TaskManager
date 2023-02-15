# Трекер Задач
Создание, обновление, удаление задач и подзадач. Возможность просмотра истории созданных задач

## Функциональность
     **addTasks** // Сохраняем таски  

    **addEpic** // Сохраняем Епики  

    **addSubtasks** // Сохраняем Сабтаски  

    **getTasks** // Получение списка всех задач  

    **deleteTask** // Очистка списка Task  

    Task getTaskByID(int taskId); // Получение задачи по айди // ИСПРАВЛЕНО+

    void updatingTask(int taskId, Task task);// Обновление Задачи по ID+

    void deleteIdTask(int taskId); // Удаление Задачи по ID+

    ArrayList<Epic> getEpic(); // Получение списка всех Эпиков // ИСПРАВЛЕНО+

    Epic getEpicByID(int epicId); // Получение Эпика по ID // ИСПРАВЛЕНО+

    void deleteEpicAndSubtask(); // Очистка списка Epic,SubTask+

    void updateEpic(int epicId, Epic epic); //Обновление Эпика+

    void deleteIdEpic(int epicId); // Удаление Эпика по ID+

    ArrayList<SubTask> getSubTaskList(); // Получение списка всех Сабтасков+

    void deleteSubTask(); // Очистка списка SubTask+

    void deleteIdSubtask(int subtaskId); // Удаление Сабтаска по ID+

    SubTask getIdSubTask(int epicId); // Получение Сабтаска по ID+

    void updateSubTask(int epicId, SubTask subTask); //Обновление Сабтаска+

    ArrayList<SubTask> getSubTaskListId(int epicId); // Получение списка всех подзадач определённого эпика.+

    void setStatusTask(int taskId, Task task); // Обновление статуса задачи // ИСПРАВЛЕНО+

    void setStatusEpic(int epicId, Epic epic); // Обновление Статуса Эпика+

    void setStatusSubtask(int epicId, SubTask subtask); // Обновление статуса Сабтаски+

    List<Task> getHistory(); // История просмотренных задач

    public ArrayList<Task> getPrioritizedTasks(); // Вывод сортировки

    void loadFromFile(File file) throws IOException;

    void save();

    boolean load();
