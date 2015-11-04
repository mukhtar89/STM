import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by Mukhtar on 11/3/2015.
 *
 * Courtesy: ElasticSearch
 */
public class AtomicArray<T> {

    private final AtomicReferenceArray<T> array;
    private volatile List<Entry<T>> nonNullList;

    /**
     * Constructor with Array size initialized
     *
     * @param size
     */
    public AtomicArray(int size) {
        array = new AtomicReferenceArray<>(size);
    }

    /**
     * Returns length of the AtomicArray
     *
     * @return int
     */
    public int length() {
        return array.length();
    }

    /**
     * Set the values of Array at index i with value T
     *
     * @param i
     * @param value
     */
    public void set(int i, T value) {
        array.set(i, value);
        if (nonNullList != null)
            nonNullList = null;
    }

    /**
     * Set the values of Array at index i with value T just once
     *
     * @param i
     * @param value
     * @throws IllegalStateException
     */
    public final void setOnce(int i, T value) {
        if (array.compareAndSet(i, null, value) == false) {
            throw new IllegalStateException("index [" + i + "] has already been set");
        }
        if (nonNullList != null) { // read first, lighter, and most times it will be null...
            nonNullList = null;
        }
    }

    /**
     * Returns the value for Array with index i
     *
     * @param i
     * @return T
     */
    public T get(int i) {
        return array.get(i);
    }

    /**
     * Returns the AtomicArray as Static List
     *
     * @return List<Entry<T>>
     */
    public List<Entry<T>> asList() {
        if (nonNullList == null) {
            if (array == null || array.length() == 0) {
                nonNullList = Collections.emptyList();
            } else {
                List<Entry<T>> list = new ArrayList<>(array.length());
                for (int i = 0; i < array.length(); i++) {
                    T e = array.get(i);
                    if (e != null) {
                        list.add(new Entry<>(i, e));
                    }
                }
                nonNullList = list;
            }
        }
        return nonNullList;
    }

    /**
     * Returns the Atomic Array as static Array
     *
     * @param a
     * @return T[]
     * @throws AtomicArrayException
     */
    public T[] toArray(T[] a) throws AtomicArrayException {
        if (a.length != array.length()) {
            throw new AtomicArrayException("AtomicArrays can only be copied to arrays of the same size");
        }
        for (int i = 0; i < array.length(); i++) {
            a[i] = array.get(i);
        }
        return a;
    }

    private static AtomicArray EMPTY = new AtomicArray(0);

    @SuppressWarnings("unchecked")
    public static <T> T empty() {
        return (T) EMPTY;
    }

    /**
     * An entry within the array.
     */
    public static class Entry<T> {
        /**
         * The original index of the value within the array.
         */
        public final int index;
        /**
         * The value.
         */
        public final T value;

        public Entry(int index, T value) {
            this.index = index;
            this.value = value;
        }
    }
}
