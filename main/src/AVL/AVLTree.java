/** Starter code for AVL Tree
 */
 
// replace package name with your netid
package AVL;

import java.util.Comparator;

public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    static class Entry<T> extends BinarySearchTree.Entry<T> {
        int height;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
    }
    
    class VerifyResult {
	    boolean flag;
	    T min;
	    T max;
	    int height;
	}
    
    AVLTree() {
	super();
    }

	// TO DO
    
    public boolean add(T x) {
    boolean ret = super.add(new Entry<T>(x, null, null));
    if(ret == false) {
    	return false;
    }
    else {
    	Entry<T> node = (Entry<T>) find(x);
    	updateHeight(node);
    }
    return true;
    }
	
	//Optional. Complete for extra credit
    public T remove(T x) {
	return super.remove(x);
    }
	
    public void updateHeight(Entry<T> node) {
        while (!stack.isEmpty()) {
            Entry<T> nums = (Entry<T>) stack.pop();
            if (nums != null) {
                nums.height = 1 + Math.max(height((Entry<T>)node.left), height((Entry<T>)node.right));
            
                int balance = balanceFactor(nums);

                if (balance > 1) {
                    if (balanceFactor((Entry<T>) nums.right) >= 0) {
                        rr(nums);
                    } 
                    else {
                        rl(nums);
                    }
                }
                else if (balance < -1) {
                    if (balanceFactor((Entry<T>) nums.left) <= 0) {
                        ll(nums);
                    } 
                    else {
                        lr(nums);
                    }
                }
            }
        }
    }


    int balanceFactor(Entry<T> node) {
    	return (height((Entry<T>)node.left) - height((Entry<T>)node.right));
    }
    
    private void rr(Entry<T> node){
    	Entry<T> temp = (Entry<T>) node.right;
        if (temp == null) {
            return; 
            }
        node.right = temp.left;
        temp.left = node;

        node.height = 1 + Math.max(height((Entry<T>)node.left), height((Entry<T>)node.right));
        temp.height = 1 + Math.max(height((Entry<T>)temp.left), height((Entry<T>)temp.right));

    }	
    
    private void rl(Entry<T> node){
    	Entry<T> right = (Entry<T>) node.right;
        if (right == null) {
            return; 
        }

        Entry<T> temp = (Entry<T>) right.left;
        if (temp == null) {
            return; 
        }

        right.left = temp.right;
        temp.right = right;
        node.right = temp.left;
        temp.left = node;

        right.height = 1 + Math.max(height((Entry<T>)right.left), height((Entry<T>)right.right));
        node.height = 1 + Math.max(height((Entry<T>) node.left), height((Entry<T>)node.right));
        temp.height = 1 + Math.max(height((Entry<T>)temp.left), height((Entry<T>)temp.right));

    }
    
    private void ll(Entry<T> node) {
        Entry<T> temp = (Entry<T>) node.left;
        if (temp == null) {
            return; 
            }
        node.left = temp.right;
        temp.right = node;

        node.height = 1 + Math.max(height((Entry<T>)node.left), height((Entry<T>)node.right));
        temp.height = 1 + Math.max(height((Entry<T>)temp.left), height((Entry<T>)temp.right));
    }

    private void lr(Entry<T> node) {
        Entry<T> left = (Entry<T>) node.left;
        if (left == null) {
            return; 
        }

        Entry<T> temp = (Entry<T>) left.right;
        if (temp == null) {
            return; 
        }

        left.right = temp.left;
        temp.left = left;
        node.left = temp.right;
        temp.right = node;

        left.height = 1 + Math.max(height((Entry<T>)left.left), height((Entry<T>)left.right));
        node.height = 1 + Math.max(height((Entry<T>) node.left), height((Entry<T>)node.right));
        temp.height = 1 + Math.max(height((Entry<T>)temp.left), height((Entry<T>)temp.right));
    }

    
    private int height(Entry<T> node) {
        return (node != null) ? node.height : -1;
    }

	
	/** TO DO
	 *	verify if the tree is a valid AVL tree, that satisfies 
	 *	all conditions of BST, and the balancing conditions of AVL trees. 
	 *	In addition, do not trust the height value stored at the nodes, and
	 *	heights of nodes have to be verified to be correct.  Make your code
	 *  as efficient as possible. HINT: Look at the bottom-up solution to verify BST
	*/
	
    boolean verify() {
        return verify((Entry<T>) super.root);
    }

    boolean verify(Entry<T> node) {
        if (node == null) {
            return true; 
        }

        int balance = balanceFactor(node);

        if (Math.abs(balance) > 1) {
            return false;
        }
        return verify((Entry<T>) node.left) && verify((Entry<T>) node.right);
    }

}
