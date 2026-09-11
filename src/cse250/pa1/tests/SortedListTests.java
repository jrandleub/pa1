package cse250.pa1.tests;

import cse250.pa1.LinkedListNode;
import cse250.pa1.SortedList;

import java.util.LinkedList;
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


    /*
    Tests if adding duplicates does NOT create a new node for each duplicate,
    but instead creates only one node and increases that nodes count
    based on how many duplicates are added.    
    */
    @Test
    public void testInsertDuplicates(){
        SortedList<Integer> list = new SortedList<>();
        list.insert(5);
        list.insert(5);
        list.insert(5);

        assertTrue(list.headNode.isPresent());
        assertFalse(list.headNode.get().next.isPresent());

        assertEquals(Integer.valueOf(5), list.headNode.get().value);
        assertEquals(3, list.headNode.get().count);
        assertEquals(3, list.length);
    }

    /*
    Tests if adding numbers in between duplicates will sort them
    exampl adding 5, 5, 4, 5 should be:
    [v: 4, c:1] <--> [v: 5, c: 3]
    */
    @Test
    public void testInsertBetweenDuplicates(){
        SortedList<Integer> list = new SortedList<>();
        list.insert(5);
        list.insert(5);
        list.insert(3);
        list.insert(5);
        list.insert(3);
        list.insert(4);

        assertTrue(list.headNode.isPresent());
        assertTrue(list.headNode.get().next.isPresent());
        assertTrue(list.headNode.get().next.get().next.isPresent());

        assertEquals(6, list.length);
        assertEquals(2, list.headNode.get().count);
        assertEquals(1, list.headNode.get().next.get().count);
        assertEquals(3, list.headNode.get().next.get().next.get().count);

        assertEquals(Integer.valueOf(3), list.get(0));
        assertEquals(Integer.valueOf(3), list.get(1));
        assertEquals(Integer.valueOf(4), list.get(2));
        assertEquals(Integer.valueOf(5), list.get(3));
        assertEquals(Integer.valueOf(5), list.get(4));
        assertEquals(Integer.valueOf(5), list.get(5));

    }

    /*
    Tests if by inserting with a hint will insert the item before the hint
    Example: a list of {3, 7} 
    insert before seven should return {3, 5, 7}
    */
    @Test
    public void testInsertWithHintBeforeHint(){
        SortedList<Integer> list = new SortedList<>();
        LinkedListNode<Integer> three = list.insert(3);
        LinkedListNode<Integer> seven = list.insert(7);

        LinkedListNode<Integer> five = list.insert(5, seven);

        assertEquals(3, list.length);

        assertEquals(Integer.valueOf(3), list.get(0));
        assertEquals(Integer.valueOf(5), list.get(1));
        assertEquals(Integer.valueOf(7), list.get(2));

        assertEquals(five, three.next.get());
        assertEquals(seven, five.next.get());

        assertEquals(three, five.prev.get());
        assertEquals(five, seven.prev.get());

        assertEquals(seven, list.lastNode.get());
        assertEquals(three, list.headNode.get());

    }


    /*
    Tests findRefBefore on a manual list of {3, 8, 15}
    Checks if searching by the exact element is present
    Checks if searching past the range, i this case 20 should return 15
    Checks if searching before the range, in this case 1 should return empty
    Finally checks if searching past the number, for example 10 should return 8
    */
    @Test
    public void testFindRefBefore(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(8, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(15, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        assertTrue(list.findRefBefore(3).isPresent());
        assertTrue(list.findRefBefore(8).isPresent());
        assertTrue(list.findRefBefore(15).isPresent());
        assertTrue(list.findRefBefore(20).isPresent());

        assertFalse(list.findRefBefore(1).isPresent());

        assertEquals(Integer.valueOf(8), list.findRefBefore(8).get().value);
        assertEquals(Integer.valueOf(8), list.findRefBefore(10).get().value);
        assertEquals(Integer.valueOf(15), list.findRefBefore(20).get().value);
    }


    /*
    Tests if searching an empty list returns empty
    */
    @Test 
    public void testFindRefBeforeEmptyList(){
        SortedList<Integer> list = new SortedList<>();
        assertEquals(Optional.empty(), list.findRefBefore(5));
    }


    /*
    Tests if searching with a hint returns the expected value
    */
    @Test
    public void testFindRefBeforeWithHint(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(8, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(15, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        assertEquals(Integer.valueOf(8), list.findRefBefore(10, first).get().value);
        assertEquals(Integer.valueOf(8), list.findRefBefore(8, third).get().value);
        assertEquals(Integer.valueOf(3), list.findRefBefore(4, third).get().value);

    }


    /*
    Tests to see if it correctly finds the given values,
    also if the value being searched is not found should return empty
    */
    @Test 
    public void testFindRef(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(8, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(15, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        assertEquals(Integer.valueOf(3), list.findRef(3).get().value);
        assertEquals(Integer.valueOf(8), list.findRef(8).get().value);
        assertEquals(Integer.valueOf(15), list.findRef(15).get().value);

        assertFalse(list.findRef(1).isPresent());
        assertFalse(list.findRef(4).isPresent());
        assertFalse(list.findRef(20).isPresent());
    }


    /*
    Tests if an empty list returns empty
    */
    @Test
    public void testFindRefEmptyList(){
        SortedList<Integer> list = new SortedList<>();
        assertEquals(Optional.empty(), list.findRef(5));
    }


    /*
    Tests if it can find ref by starting at both head and tail
    also checks if a value that doesnt exist returns empty 
    */
    @Test
    public void testFindRefWithHint(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(8, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(15, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        assertEquals(Integer.valueOf(15), list.findRef(15, first).get().value);
        assertEquals(Integer.valueOf(3), list.findRef(3, third).get().value);
        assertFalse(list.findRef(10, first).isPresent());
    }


    /*
    Tests if get() throws an outofbounds error when index is 
    less than 0 or >= length of list
    */
    @Test
    public void testGetOutOfBounds(){
        LinkedListNode<Integer> first = new LinkedListNode<>(1, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(2, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(3, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        try{
            list.get(-1);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }

        try{
            list.get(3);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }
    }


    /*
    Tests if get returns correct value on a node with a single
    count and a node with count of 2
    */
    @Test
    public void testGetRetturnCorrectValue(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 2);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 3;

        assertEquals(Integer.valueOf(3), list.get(0));
        assertEquals(Integer.valueOf(5), list.get(1));
        assertEquals(Integer.valueOf(5), list.get(2));
    }

    /*
    Test get on an empty list
    */
    @Test
    public void testGetEmptyList(){
        SortedList<Integer> list = new SortedList<>();

        try{
            list.get(0);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }

    }

    /*
    Tests that getRef throws out of bounds when index is 
    less than 0
    or >= length of list
    */
    @Test public void testGetRefOutOfBounds(){
        LinkedListNode<Integer> first = new LinkedListNode<>(1, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(2, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(3, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        try{
            list.getRef(-1);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }

        try{
            list.getRef(3);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }
    }

    /*
    Tests that getRef throws out of bounds on empty list
    */
    @Test
    public void testGetRefEmptyList(){
        SortedList<Integer> list = new SortedList<>();

        try{
            list.getRef(0);
            fail();
        }
        catch(IndexOutOfBoundsException e){
            // expected
        }
    }

    /*
    Tests that getRef returns the correct node when count is 1
    and when count is 2
    */
    @Test
    public void testGetRefReturnsCorrectNode(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 2);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 3;

        assertEquals(first, list.getRef(0));
        assertEquals(second, list.getRef(1));
        assertEquals(second, list.getRef(2));
    }

    /*
    Test that removing one copy from a duplicate
    decrements that nodes count by ref
    */
    @Test
    public void testRemoveOneCopyFromDuplicate(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 2);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 3;

        assertEquals(Integer.valueOf(5), list.remove(second));

        assertEquals(2, list.length);
        assertEquals(1, second.count);
    }

    /*
    Test if the node has count of one, the whole
    node gets removed from the list
    */
   @Test
   public void testRemoveNode(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 1);
		LinkedListNode<Integer> third = new LinkedListNode<>(7, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 3;

        assertEquals(Integer.valueOf(5), list.remove(second));

        assertEquals(2, list.length);
        assertEquals(third, first.next.get());
        assertEquals(first, third.prev.get());
   }

   /*
    Test if the node has count of one, the whole
    node gets removed from the list
    */
   @Test
   public void testRemoveOnlyNodeInList(){
        LinkedListNode<Integer> first = new LinkedListNode<>(5, 1);
        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(first);
        list.length = 1;

        assertEquals(Integer.valueOf(5), list.remove(first));

        assertEquals(0, list.length);
        assertFalse(list.headNode.isPresent());
        assertFalse(list.lastNode.isPresent());
   }

   /*
    Test that removing one copy from a duplicate
    decrements that nodes count by ref
    */
    @Test
    public void testRemoveNCopiesFromDuplicate(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 3);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 4;

        assertEquals(Integer.valueOf(5), list.removeN(second, 2));

        assertEquals(2, list.length);
        assertEquals(1, second.count);

        assertEquals(second, first.next.get());
        assertEquals(first, second.prev.get());
        assertEquals(second, list.lastNode.get());
    }

    @Test
    public void removeNIllegalArgumentException(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 3);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 4;

        try{
            list.removeN(second, 4);
            fail();
        }
        catch(IllegalArgumentException e){
            // expected
        }

        assertEquals(3, second.count);
        assertEquals(4, list.length);
        assertEquals(second, first.next.get());
    }

    @Test
    public void testRemoveNExactAmount(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 3);
		LinkedListNode<Integer> third = new LinkedListNode<>(7, 2);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 6;

        assertEquals(Integer.valueOf(5), list.removeN(second, 3));

        assertEquals(3, list.length);
        assertEquals(third, first.next.get());
        assertEquals(first, third.prev.get());

    }

    @Test
    public void testRemoveAll(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 3);
		LinkedListNode<Integer> third = new LinkedListNode<>(7, 2);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);
        second.next = Optional.of(third);
        third.prev = Optional.of(second);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(third);
        list.length = 6;

        assertEquals(Integer.valueOf(5), list.removeAll(second));

        assertEquals(3, list.length);
        assertEquals(third, first.next.get());
        assertEquals(first, third.prev.get());

    }

    @Test
    public void testRemoveAllEverythingFromList(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3,2);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(first);
        list.length = 2;

        assertEquals(Integer.valueOf(3), list.removeAll(first));

        assertEquals(0, list.length);
        assertFalse(list.headNode.isPresent());
        assertFalse(list.lastNode.isPresent());
    }

    @Test
    public void removeAllHeadNode(){
        LinkedListNode<Integer> first = new LinkedListNode<>(3, 1);
		LinkedListNode<Integer> second = new LinkedListNode<>(5, 1);

        first.next = Optional.of(second);
        second.prev = Optional.of(first);

        SortedList<Integer> list = new SortedList<>();
        list.headNode = Optional.of(first);
        list.lastNode = Optional.of(second);
        list.length = 2;

        assertEquals(Integer.valueOf(3), list.removeAll(first));
        assertEquals(1, list.length);
        assertEquals(second, list.headNode.get());
        assertEquals(second, list.lastNode.get());
        assertFalse(second.prev.isPresent());
        assertFalse(second.next.isPresent());

    }

}
