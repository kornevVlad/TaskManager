package manager;
import task.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Node<Task> first;
    Node<Task> last;

    protected final Map<Integer, Node> linkedMap = new HashMap<>();
    protected CustomLinkedList<Task> linkedList = new CustomLinkedList<>();

    @Override
    public void addHistory(Task task) { // Сохранение истории просмотров
        if(task != null) {
            if (!linkedMap.containsKey(task.getId())) { // Дубля нет
                linkedList.linkLast(task);
                linkedMap.put(task.getId(), last);
            } else { // Дубль есть
                linkedList.removeNode(task.getId());
                linkedList.linkLast(task);
                linkedMap.put(task.getId(), last);
            }

        }else {
            return;
        }
    }

    @Override
    public List<Task> getHistory() { // Вывод элемнтов истории
       return linkedList.getTask(); // Вывод истории просмотров
    }

    @Override
    public void remove(int id) { // Удаление задачи из Истории просмотров
        if(linkedMap.containsKey(id)){
            linkedList.removeNode(id);  // Перепривязка ссылок
            linkedMap.remove(id);
        }
    }

    public class CustomLinkedList<Task>  {

        public void linkLast(task.Task task) { // Добавление Node в конец списка
            final Node<task.Task> l = last;
            final Node<task.Task> newNode = new Node<task.Task>(l, task, null);
            last = newNode;
            if (l == null) {
                first = newNode;
            }else{
                l.next = newNode;
            }
        }

        public void removeNode(int id){ // Удаление Повторной Ноды задачи и отвязка ссылок
            Node node = linkedMap.get(id);
            if(node.prev == null && node.next == null){
                first = null;
                last = null;
            }
            else if(node.prev != null && node.next !=null){
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
            else if(node.next != null){
                first = node.next;
                first.prev = null;
            }
            else if(node.prev != null){
                last = node.prev;
                last.next = null;
            }
        }

        public List<task.Task> getTask() { // Сохранение истории вызова метода addHistory   !!!ИСПРАВЛЕНО!!!
            List<task.Task> history = new ArrayList<>();
            Node node = first;
            while (node != null) {
                history.add((task.Task) node.task);
                node = node.next;
            }
            return history;
        }
    }
} 