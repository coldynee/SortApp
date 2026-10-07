package CustomArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class CustomArrayList<E> implements Collection<E> {

	private E[] elements;
    private int size = 0;
    
    public CustomArrayList() {
    	elements = (E[]) new Object[2];
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		
		//return Arrays.copyOf(elements, size, a);
		return null;
	}

	@Override
	public boolean add(E e) {
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, size * 2);
		}
		elements[size] = e;
		size++;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		var isFound = false;
		for (int i = 0; i < size; i++) {
			if (isFound) {
				elements[i-1] = elements[i];
			}
			if (Objects.equals(elements[i], o)) {
				isFound = true;
				size--;
			}
		}
		return isFound;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}


