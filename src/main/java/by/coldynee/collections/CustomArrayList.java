package by.coldynee.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Кастомная реализация динамического массива, аналогичная {@link java.util.ArrayList}.
 * <p>
 * Реализует интерфейс {@link List} для полной совместимости со стандартными коллекциями Java.
 * Использует внутренний массив с автоматическим увеличением размера при переполнении.
 * </p>
 * <p>
 * <b>Особенности реализации:</b>
 * <ul>
 *   <li>Начальная вместимость: 2 элемента</li>
 *   <li>Коэффициент роста: 2x при переполнении</li>
 *   <li>Использует {@link System#arraycopy} для быстрого сдвига элементов</li>
 *   <li>Защищена от утечек памяти (зануление ссылок при удалении)</li>
 * </ul>
 * </p>
 *
 * @param <T> тип элементов в списке
 */
public class CustomArrayList<T> implements List<T> {

    private T[] elements;
    private int size = 0;

    /**
     * Создает пустой список с начальной вместимостью 2 элемента.
     */
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
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает итератор для последовательного доступа к элементам.
     * Поддерживает безопасное удаление элементов во время итерации.
     */
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
        if (o == null) return false;
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

    /**
     * Удаляет все элементы, которые присутствуют в указанной коллекции.
     * Использует обратный цикл для безопасного удаления без сдвига индексов.
     */
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

    /**
     * Оставляет только те элементы, которые присутствуют в указанной коллекции.
     * Использует обратный цикл для безопасного удаления.
     */
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

    /**
     * Очищает список, зануляя все ссылки для предотвращения утечек памяти.
     */
	@Override
	public void clear() {
		for (int i = 0; i < elements.length; i++)
		{
			elements[i] = null;
		}
		size = 0;
	}

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldElement = (T) elements[index];
        elements[index] = element;
        return oldElement;
    }

    /**
     * Вставляет элемент по указанному индексу, сдвигая все последующие элементы вправо.
     */
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }
        elements[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldElement = (T) elements[index];
        removeAt(index);
        return oldElement;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) return -1;
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    /**
     * Возвращает двунаправленный итератор, начиная с указанного индекса.
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            private int cursor = index;
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
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public T previous() {
                if (cursor <= 0) {
                    throw new NoSuchElementException();
                }
                lastRet = --cursor;
                return (T) elements[cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
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

            @Override
            public void set(T e) {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                CustomArrayList.this.set(lastRet, e);
            }

            @Override
            public void add(T e) {
                CustomArrayList.this.add(cursor++, e);
                lastRet = -1;
            }
        };
    }

    /**
     * Возвращает представление части списка от fromIndex (включительно) до toIndex (исключительно).
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        CustomArrayList<T> subList = new CustomArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add((T) elements[i]);
        }
        return subList;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        boolean modified = false;
        for (T item : c) {
            add(index++, item);
            modified = true;
        }
        return modified;
    }

    /**
     * Удаляет элемент по индексу и сдвигает все последующие элементы влево.
     * Зануляет последнюю ячейку для предотвращения утечек памяти.
     */
	private void removeAt(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
    }

    /**
     * Увеличивает вместимость массива в 2 раза, если достигнут лимит.
     * Использует амортизированную сложность O(1) для операции добавления.
     */
	private void ensureCapacity() {
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, size * 2);
		}
    }
}

