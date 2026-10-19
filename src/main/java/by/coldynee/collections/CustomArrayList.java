
package by.coldynee.collections;

import java.util.*;

public class CustomArrayList<T> implements Collection<T> {

    private T[] elements;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public CustomArrayList() {
        elements = (T[]) new Object[2];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }


            @Override
            public T next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return (T) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                CustomArrayList.this.removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            return (E[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(T e) {
        ensureCapacity();
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T item : c) {
            if (add(item)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
        {
            elements[i] = null;
        }
        size = 0;
    }

    private void removeAt(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, size * 2);
        }
    }

    public long countOccurrencesStream(T target) {
        return Arrays.stream(elements, 0, size)
                .parallel()
                .filter(e -> Objects.equals(e, target))
                .count();
    }
}