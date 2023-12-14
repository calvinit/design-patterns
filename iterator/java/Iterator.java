import java.util.*;

interface MyList<E> {
    void add(E e);

    void remove(int index);

    E get(int index);

    int size();

    default MyIterator<E> iterator() {
        return new Itr<>(this);
    }
}

class MyArrayList<E> implements MyList<E> {
    private transient Object[] elements;
    private int size;
    transient int modCount; // 修改次数，每 add 或 remove 一次就加 1

    public MyArrayList() {
        elements = new Object[10];
    }

    @Override
    public void add(E e) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = e;
        modCount++;
    }

    private void grow() {
        Object[] newElements = new Object[elements.length + 10];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Index out of range: " + index + ", size: " + size);
        }
        int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(elements, index + 1, elements, index, newSize - index);
        }
        elements[size = newSize] = null;
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Index out of range: " + index + ", size: " + size);
        }
        return (E) elements[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MyIterator<E> iterator() {
        return new MyArrayItr<>(this, modCount);
    }

    @Override
    public String toString() {
        List<Object> elements = new ArrayList<>(size);
        elements.addAll(Arrays.asList(this.elements).subList(0, size));
        return elements.toString();
    }
}

class MyLinkedList<E> implements MyList<E> {
    private transient Node<E> first;
    private transient Node<E> last;
    private transient int size;
    transient int modCount; // 修改次数，每 add 或 remove 一次就加 1

    @Override
    public void add(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Index out of range: " + index + ", size: " + size);
        }
        Node<E> x = node(index);
        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.element = null;
        size--;
        modCount++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Index out of range: " + index + ", size: " + size);
        }
        return node(index).element;
    }

    private Node<E> node(int index) {
        Node<E> node;
        if (index < (size >> 1)) {
            // 从前往后搜索
            int i = 0;
            node = first;
            while ((i++) < index) {
                node = node.next;
            }
        } else {
            // 从后往前搜索
            int i = size - 1;
            node = last;
            while ((i--) > index) {
                node = node.prev;
            }
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MyIterator<E> iterator() {
        return new MyLinkedItr<>(this, modCount);
    }

    @Override
    public String toString() {
        Node<E> node = first;
        if (node == null) {
            return "[]";
        }
        List<E> elements = new ArrayList<>(size);
        elements.add(node.element);
        int i = 1;
        while ((i++) < size) {
            elements.add(node(i).element);
        }
        return elements.toString();
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;
        private Node<E> prev;

        public Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }
    }
}

// ============================================================

interface MyIterator<E> {
    boolean hasNext();

    E next();

    void remove();
}

class Itr<E> implements MyIterator<E> {
    private int cursor;             // 当前遍历到的元素游标
    private int lastRet = -1;       // 指向当前游标的前一个元素，-1 表示没有
    final MyList<E> list;

    public Itr(MyList<E> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return cursor != list.size(); // 注意这里：cursor 在指向最后一个元素的时候，hasNext() 仍旧返回 true
    }

    @Override
    public E next() {
        if (cursor >= list.size()) {
            throw new NoSuchElementException("Overflow cursor: " + cursor);
        }
        lastRet = cursor++;
        return list.get(lastRet);
    }

    @Override
    public void remove() {
        if (lastRet < 0) {
            throw new IllegalStateException("next() is neccessary before remove()");
        }
        list.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
    }
}

class MyArrayItr<E> extends Itr<E> {
    private int expectedModCount;   // 期望的修改次数，当和 MyList 的 modCount 不一致时，说明在遍历过程中 MyList 已经发生了改变

    public MyArrayItr(MyArrayList<E> list, int modCount) {
        super(list);
        this.expectedModCount = modCount;
    }

    @Override
    public E next() {
        checkForComodification();
        return super.next();
    }

    @Override
    public void remove() {
        checkForComodification();
        super.remove();
        expectedModCount = ((MyArrayList<E>) list).modCount;
    }

    private void checkForComodification() {
        if (((MyArrayList<E>) list).modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
}

class MyLinkedItr<E> extends Itr<E> {
    private int expectedModCount;   // 期望的修改次数，当和 MyList 的 modCount 不一致时，说明在遍历过程中 MyList 已经发生了改变

    public MyLinkedItr(MyLinkedList<E> list, int modCount) {
        super(list);
        this.expectedModCount = modCount;
    }

    @Override
    public E next() {
        checkForComodification();
        return super.next();
    }

    @Override
    public void remove() {
        checkForComodification();
        super.remove();
        expectedModCount = ((MyLinkedList<E>) list).modCount;
    }

    private void checkForComodification() {
        if (((MyLinkedList<E>) list).modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
}