package manager;

public class Node<Task> {
    Node prev;
    Task task;
    Node next;

    public Node(Node<Task> prev, Task task, Node<Task> next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "prev=" + (prev != null ? prev.task : null) +
                ", task=" + task +
                ", next=" + (next != null ? next.task : null) +
                '}';
    }
}