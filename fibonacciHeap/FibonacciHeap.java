package fibonacciHeap;

import linkedList.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

public class FibonacciHeap<T> {

	private HeapNode min;
	private LinkedList<HeapNode> roots;
	private int size;
	
	public FibonacciHeap(){
		min = null;
		roots = new LinkedList<HeapNode>();
		size = 0;
	}
	
	private FibonacciHeap(LinkedList<HeapNode> lst){
		min = lst.getHead().getVal();
		roots = lst;
		size = 1;
	}
	
	/**
	 * @return number of nodes in heap
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @param otr
	 * this method doesn't change a lot in the structure of the heap
	 * it concats the roots lists and updates min and size
	 */
	public void union(FibonacciHeap<T> otr) {
		this.roots.concat(otr.roots);
		size += otr.size;
		if(otr.min == null) return;
		if(this.min == null) {
			min = otr.min;
			return;
		}
		if(otr.min.key < this.min.key) min = otr.min;
	}

	/**
	 * @param val
	 * @param key
	 * creates a new heap of one node and then unites it with this
	 */
	public void add(T val, int key) {
		HeapNode nd = new HeapNode(val, key);
		FibonacciHeap<T> tmp = new FibonacciHeap<T>(new LinkedList<HeapNode>(nd));
		nd.nodeInPar = tmp.roots.getHead();
		this.union(tmp);
	}
	
	/**
	 * @return value of min
	 * no changes to the structure of the heap
	 */
	public HeapNode getMin() {
		return min;
	}
	
	/**
	 * @return min of heap
	 * it only returns the value without the key. this can be modified by importing Pair
	 */
	public T extractMin() {
		T res = min.val;
		this.deleteMin();
		return res;
	}
	
	public void deleteMin() {
		if(size <= 1) {
			min = null;
			roots = new LinkedList<HeapNode>();
			size = 0;
			return;
		}
		min.children.forEach(nd -> min.deleteChild(nd.nodeInPar));
		roots.delete(min.nodeInPar);
		min = roots.getHead().getVal();
		consolodate();
		size--;
	}
	
	//the main event...
	/**
	 * reorganizes the heap.
	 * this is a "heavy" method that takes all the slack for the sake of amortization
	 */
	private void consolodate() {
		ArrayList<HeapNode> addRack = new ArrayList<HeapNode>();
		int n = Integer.SIZE - Integer.numberOfLeadingZeros(size); //log on base 2 of size
		for(int i = 0; i <= n; i++) {
			addRack.add(null);
		}
		Iterator<HeapNode> iter = roots.iterator();
		HeapNode curr;
		HeapNode otrCurr;
		int d;
		while(iter.hasNext()) {
			curr = iter.next();
			d = curr.children.size();
			while(addRack.get(d) != null) {
				otrCurr = addRack.get(d);
				if(curr.key > otrCurr.key) {
					HeapNode tmp = otrCurr;
					otrCurr = curr;
					curr = tmp;
				}
				curr.link(otrCurr);
				addRack.set(d, null);
				d++;
			}
			addRack.set(d, curr);
		}
		roots = new LinkedList<HeapNode>();
		remakeRoots(addRack);
	}
	
	/**
	 * @param addRack
	 * takes the trees in addRack and makes them roots of the heap
	 */
	private void remakeRoots(ArrayList<HeapNode> addRack){
		addRack.forEach(nd -> {if(nd != null) addAndUpdate(nd);});
	}
	
	/**
	 * @param node
	 * makes node a root in the heap and updates min if necessary
	 */
	private void addAndUpdate(HeapNode node) {
		roots.add(node);
		node.nodeInPar = roots.getHead();
		node.parent = null;
		if(node.cut) node.cut = false;
		if(node.key < min.key) min = node;
	}
	
	public class HeapNode{
		
		private T val;
		private int key;
		private HeapNode parent;
		private LinkedList<HeapNode> children;
		private LinkedList<HeapNode>.ListNode nodeInPar;
		private boolean cut;
		private int size;
		
		/**
		 * @param val
		 * @param key
		 * note! the builder does not take care of: parent and nodeInPar
		 * you must do this manually
		 */
		public HeapNode(T val, int key) {
			this.val = val;
			this.key = key;
			children = new LinkedList<HeapNode>();
			cut = false;
			size = 1;
		}
		
		/**
		 * @param dec
		 * decreases the key of this by the value dec, and rearranges the heap accordingly
		 *has not been tested!!
		 */
		public void decreaseKey(int dec) {
			key -= dec;
			if(parent != null && key < parent.key) {
				parent.deleteChild(nodeInPar);
				cut = false;
			}
			if(key < min.key) min = this;
		}
		
		/**
		 * @param child
		 * disconnects child from this and makes it another root in the heap
		 */
		private void deleteChild(LinkedList<HeapNode>.ListNode child) {
			children.delete(child);
			HeapNode node = child.getVal();
			size -= node.size;
			roots.add(node);
			node.nodeInPar = roots.getHead();
			node.parent = null;
			if(cut) {
				parent.deleteChild(nodeInPar);
				cut = false;
			}
			else{
				if(parent != null) cut = true;
			}
		}
		
		/**
		 * @param otr
		 * makes otr a new child of this
		 */
		private void link(HeapNode otr) {
			children.add(otr);
			otr.nodeInPar = children.getHead();
			otr.parent = this;
			size += otr.size;
		}
	}
}
