/**
 * @author rbk, sa
 * Binary search tree (starter code)
 **/

// replace package name with your netid
package AVL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
	
	public static void main(String[] args) throws FileNotFoundException {
        BinarySearchTree<Long> bst = new BinarySearchTree<>();
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        // Initialize the timer
        Timer timer = new Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add": {
                    operand = sc.nextInt();
                    if (bst.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextInt();
                    if (bst.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Contains": {
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
            }
        }
        sc.close();
        // End Time
        timer.end();

        System.out.println(result);
        System.out.println(timer);
    }


    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
    
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;
    ArrayDeque<Entry<T>> stack = null;
    
    public BinarySearchTree() {
        root = null;
        size = 0;
        stack = new ArrayDeque<>();
    }

    public Entry<T> find(T x) {
    	stack.clear();
        return find(root,x);
    }

    public Entry<T> find(Entry<T> node, T x) {
    	if(node == null || node.element == x) {
        	return node;
        }
    	while(true) {
    		if(x.compareTo(node.element) < 0) {
    			if(node.left == null) {
    				break;
    			}
    			stack.push(node);
    			node = node.left;
    		}
    		else if(x.compareTo(node.element) == 0) {
    			break;
    		}
    		else if(node.right == null) {
    			break;
    		}
    		else {
    			stack.push(node);
    			node = node.right;
    		}
    	}
    	return node;
    }
    
    /** TO DO: Is x contained in tree?
     */
    public boolean contains(T x) {
    	if(root == null) {
    		return false;
    	}
        Entry<T> current = find(x);
        if (current.equals(null) || !current.element.equals(x)) {
            return false;
        }

        return true;
    }



    /** TO DO: Add x to tree. 
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        if(size == 0) {
        	root = new Entry<T>(x,null,null);
        	size++;
        }
        else {
        	Entry<T> current = find(x);
        	if(current.element.equals(x)) {
        		current.element = x;
        		return false;
        	}
        	if(x.compareTo(current.element) < 0) {
        		current.left = new Entry<T>(x, null, null);
        	}
        	else {
        		current.right = new Entry<T>(x, null, null);
        	}
            size++;
        }
        stack.clear();
        return true;
    }
    
    public boolean add(Entry<T> x) {
        if(size == 0) {
        	root = x;
        	size++;
        }
        else {
        	Entry<T> current = find(x.element);
        	if(current.element.equals(x.element)) {
        		current.element = x.element;
        		return false;
        	}
        	if(x.element.compareTo(current.element) < 0) {
        		current.left = x;
        	}
        	else {
        		current.right = x;
        	}
            size++;
        }
        stack.clear();
        return true;
    }


    /** TO DO: Remove x from tree. 
     *  Return x if found, otherwise return null
     */
    public T remove(T x) {
    	if(size == 0) {
    		return null;
    	}
    	Entry<T> current = find(x);
    	if(!current.element.equals(x)) {
    		return null;
    	}
    	if(current.left == null || current.right == null) {
    		splice(current);
    	}
    	else {
    		stack.push(current);
    		Entry<T> minRight = find(current.right, x);
    		current.element = minRight.element;
    		splice(minRight);
    	}
    	size--;
        stack.clear();
    	return x;
    }

    public T remove(Entry<T> x) {
    	if(size == 0) {
    		return null;
    	}
    	Entry<T> current = find(x.element);
    	if(!current.element.equals(x.element)){
    		return null;
    	}
    	if(current.left == null || current.right == null) {
    		splice(current);
    	}
    	else {
    		stack.push(current);
    		Entry<T> minRight = find(current.right, x.element);
    		current.element = minRight.element;
    		splice(minRight);
    	}
    	size--;
        stack.clear();
    	return x.element;
    }
    
    public void splice(Entry<T> node) {
    	Entry<T> current = stack.peek();
    	Entry<T> kids = (node.left == null ? node.right:node.left);
    	if(current == null) {
    		root = kids;
    	}
    	else if(current.left == node) {
    		current.left = kids;
    	}
    	else
    		current.right = kids;
    }

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}





