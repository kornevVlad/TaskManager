package manager;

import task.Task;
import java.util.List;


public interface HistoryManager  {

   void addHistory(Task task); // Запись просмотров по ID

   List<Task> getHistory(); // Получение спикска Истории Задач

   void remove(int id); // Удаление через LinkedList
}