/**
 * this whole package was created for the sake of the Fibonacci heap
 * things here may be less than optimal
 */
package linkedList;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T>{
	
	private ListNode head;
	private ListNode last;
	private int size;
	
	public LinkedList(){
		head = last = null;
		size = 0;
	}
	
	public LinkedList(T elem){
		head = last = new ListNode(elem);
		size = 1;
	}
	
	public int size() {
		return size;
	}
	
	public void add(T val) {
		ListNode tmp = new ListNode(val);
		tmp.next = head;
		if(head != null) head.prev = tmp;
		head = tmp;
		if(last == null) {
			last = head;
		}
		size++;
	}
	
	public void delete(ListNode node) {
		if(node.prev == null) head = node.next;
		else node.prev.next = node.next;
		if(node.next == null) last = node.prev;
		else node.next.prev = node.prev;
		size--;
	}
	
	public void concat(LinkedList<T> otr){
		if(otr.size == 0) return;
		if(size == 0) {
			this.head = otr.head;
			this.last = otr.last;
			this.size = otr.size;
			return;
		}
		last.next = otr.head;
		otr.head.prev = last;
		last = otr.last;
		size += otr.size;
	}
	
	@Override
	public Iterator<T> iterator(){
		return new LinkedListIterator<T>(head);
	}
	
	public ListNode getHead() {
		return head;
	}
	
	public class ListNode{
		private T val;
		private ListNode next;
		private ListNode prev;
		
		public ListNode(T val) {
			this.val = val;
			next = prev = null;
		}
		
		public T getVal() {
			return val;
		}
		
		protected ListNode getNext() {
			return next;
		}
	}
}
