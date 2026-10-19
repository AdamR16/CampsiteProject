package org.example;

import java.util.Iterator;

public class myList<T> implements Iterable<T> {

    private class Node {
        private T contents;
        private Node next;

        Node(T contents) {
            this.contents = contents;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public void add(T element) {
        Node n = new Node(element);
        if (head == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public void addFirst(T element) {
        Node n = new Node(element);
        n.next = head;
        head = n;
        if (tail == null) {
            tail = n;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.contents;
    }

    public boolean remove(T element) {
        Node previous = null;
        Node current = head;

        while (current != null) {
            if (sameItem(current.contents, element)) {
                if (previous == null) {
                    head = current.next;         // case 1: removing the head
                } else {
                    previous.next = current.next; // case 2/3: skip over current
                }
                if (current == tail) {
                    tail = previous;             // case 3: removing the tail, move tail back
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public int indexOf(T element) {
        int index = 0;
        for (Node n = head; n != null; n = n.next) {
            if (sameItem(n.contents, element)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private boolean sameItem(T a, T b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = head;   // starts at the head

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T item = current.contents;
                current = current.next;    // move along the chain
                return item;
            }
        };
    }
}
