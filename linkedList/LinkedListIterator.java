package linkedList;

import java.util.Iterator;

public class LinkedListIterator<T> implements Iterator<T>{

	private LinkedList<T>.ListNode next;
	
	public LinkedListIterator(LinkedList<T>.ListNode head){
		next = head;
	}
	
	@Override
	public boolean hasNext() {
		return next != null;
	}
	
	@Override
	public T next(){
		LinkedList<T>.ListNode res = next;
		if(hasNext()) next = next.getNext();
		return res.getVal();
	}
	
	/**
	 * didn't really need to implement this method for the sake of the heap...
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
