package cse250.pa1.tests;

import cse250.pa1.LinkedListNode;
import cse250.pa1.SortedList;

import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.*;

public class SortedListTests {
    @Test
    public void testInsertInOrderElementsInOrder() {
        SortedList<Integer> list = new SortedList<>();

        for (int i = 0; i < 10; i++) {
            list.insert(i);
        }

        int index = 0;
        for (Integer elem : list) {
            assertEquals(Integer.valueOf(index), elem);
            index++;
        }
    }

    @Test
    public void testInsertReverseOrderElementsInOrder() {
        SortedList<Integer> list = new SortedList<>();

        for (int i = 9; i >= 0; i--) {
            list.insert(i);
        }

        int index = 0;
        for (Integer elem : list) {
            assertEquals(Integer.valueOf(index), elem);
            index++;
        }
    }

    @Test
    public void testInsertInOrderElementsWithHints() {
        SortedList<Integer> list = new SortedList<>();

        LinkedListNode<Integer> tail = list.insert(0);
        for (int i = 1; i < 100; i++) {
            tail = list.insert(i, tail);
        }

        int index = 0;
        for (Integer elem : list) {
            assertEquals(Integer.valueOf(index), elem);
            index++;
        }
    }

	@Test
	public void testManuallyBuiltList() {
		LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(8, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(15, 1);

		first.next = Optional.of(second);

		second.prev = Optional.of(first);
		second.next = Optional.of(third);

		third.prev = Optional.of(second);

		SortedList<Integer> list = new SortedList<>();
		list.length = 3;
		list.headNode = Optional.of(first);
		list.lastNode = Optional.of(third);

        assertEquals(second, list.getRef(1));
	}
}
