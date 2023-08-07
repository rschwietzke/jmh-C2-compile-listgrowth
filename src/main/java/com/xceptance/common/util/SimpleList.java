/*
 * Copyright (c) 2005-2022 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Inexpensive (partial) list implementation. Not fully implemented, just what is needed. As soon as
 * iterators and other things are involved, the memory savings we wanted are gone.
 *
 * Minimal checks for data correctness!! This is tuned for speed not elegance or safety.
 *
 * @author Rene Schwietzke
 * @since 7.0.0
 */
public class SimpleList<T>
{
    private T[] data;
    private int size;

    /**
     * Creates a new list wrapper from an existing one. This is not copying anything rather
     * referencing it. Make sure that you understand that!
     *
     * @param list
     */
    SimpleList(final SimpleList<T> list)
    {
        data = list.data;
        size = list.size;
    }

    /**
     * Create a new list with a default capacity.
     * @param capacity the capacity
     */
    public SimpleList(final int capacity)
    {
        data = (T[]) new Object[capacity];
    }

    /**
     * Add an element to the end of the list
     *
     * @param element the element to add
     * @return true if added and for this impl it is always true
     */
    public boolean add(T element)
    {
        if (size == data.length)
        {
            // avoid slow growth in the beginning and add 3 to avoid
            // 1, 2, 4, 8, 16 and go with 1, 4, 10, 22, 46 instead
            data = Arrays.copyOf(data, (data.length << 1) + 2);
        }

        data[size] = element;
        size++;

        return true;
    }

    /**
     * Add an element to the end of the list without a range check
     *
     * @param element the element to add
     * @return true if added and for this impl it is always true
     */
    public boolean addUnsafe(T element)
    {
        data[size] = element;
        size++;

        return true;
    }

    private void check()
    {
        if (size == data.length)
        {
            data = Arrays.copyOf(data, data.length << 1);
        }
    }

    public void increaseCapacity()
    {
        data = Arrays.copyOf(data, data.length << 1);
    }

    /**
     * Add an element to the end of the list
     *
     * @param element the element to add
     * @return true if added and for this impl it is always true
     */
    public boolean add2(T element)
    {
        check();

        data[size] = element;
        size++;

        return true;
    }

    private T[] getData()
    {
        if (size == data.length)
        {
            data = Arrays.copyOf(data, data.length << 1);
        }

        return data;
    }

    /**
     * Add an element to the end of the list
     *
     * @param element the element to add
     * @return true if added and for this impl it is always true
     */
    public boolean add3(T element)
    {
        final T[] d = getData();

        d[size] = element;
        size++;

        return true;
    }

    /**
     * Return an element at index. No range checks at all.
     *
     * @param index the position
     * @return the element at this position
     */
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        return (T) data[index];
    }

    /**
     * Returns the size of this list
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns the size of this list
     */
    public boolean reachedCapacity()
    {
        return size == data.length;
    }

    /**
     * Creates an array of the elements. This is a copy operation!
     *
     * @return an array of the elements
     */
    public Object[] toArray()
    {
        return Arrays.copyOf(data, size);
    }

    /**
     * Creates an array of the elements. This is a copy operation!
     *
     * @return an array of the elements
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array)
    {
        return (T[]) Arrays.copyOf(data, size, array.getClass());
    }

    /**
     * Clears the list by setting the size to zero. It does not release any
     * elements for performance purposes.
     */
    public void clear()
    {
        // are are not releasing any references, this is because of speed aka less memory
        // access needed
        size = 0;
    }

    /**
     * Returns view partitions on the underlying list. If the count is larger than size
     * you get back the maximum possible list number with one element each. If count
     * is 0 or smaller, we correct it to 1.
     *
     * @param count how many list do we want
     * @return a list of lists
     */
    public SimpleList<SimpleList<T>> partition(int count)
    {
        final int _count;
        if (count > size)
        {
            _count = size;
        }
        else
        {
            _count = count <= 0 ? 1 : count;
        }

        final SimpleList<SimpleList<T>> result = new SimpleList<>(count);

        final int newSize = (int) Math.ceil((double) size / (double) _count);
        for (int i = 0; i < _count; i++)
        {
            int from = i * newSize;
            int to = from + newSize - 1;
            if (to >= size)
            {
                to = size - 1;
            }
            result.add(new Partition<>(this, from, to));
        }

        return result;
    }

    class Partition<K> extends SimpleList<K>
    {
        private final int from;
        private final int size;

        public Partition(final SimpleList<K> list, final int from, final int to)
        {
            super(list);

            this.from = from;
            this.size = to - from + 1;
        }

        public boolean add(K o)
        {
            throw new RuntimeException("Cannot modify the partition");
        }

        public K get(int index)
        {
            return (K) super.get(index + from);
        }

        public int size()
        {
            return size;
        }

        public K[] toArray()
        {
            throw new RuntimeException("Cannot modify the partition");
        }

    }
}
