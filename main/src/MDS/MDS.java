/** Starter code for P3
 *  @author
 */

// Change to your net id
package MDS;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;

class Item implements Comparable<Item> {
    int id, price;
    List<Integer> description;

    public Item(int id, int price, List<Integer> description) {
        this.id = id;
        this.price = price;
        this.description = new LinkedList<>(description);
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getId() {
    	return id;
    }
    
    public int compareTo(Item item) { // Used to Compare the Items based on cost, and if its the same, then ID.
    	if(this.price < item.price) {
    		return -1;
    	}
    	else if(this.price > item.price) {
    		return 1;
    	}
    	else {
    		return Integer.compare(this.id, item.id);
    	}
    }
}


public class MDS {
	int size;
	TreeMap<Integer, Item> tree;
	HashMap<Integer, TreeSet<Item>> map;
    // Constructors
	
    public MDS() {
    	tree = new TreeMap<>();
    	map = new HashMap<>();
    	
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
    */
    public int insert(int id, int price, List<Integer> list) {
        if (tree.containsKey(id)) { // If it contains the ID, then just replace everything in it
        	Item item = tree.get(id);
            deleteDesc(item);
            tree.remove(id);
        	if(list!=null && !(list.isEmpty())) { // Edge case for if the description shouldn't be edited
        		item.description.clear();
        		item.description.addAll(list);
        	}
            item.id = id;
            item.price = price;
            tree.put(id, item);
            updateDesc(item);
            return 0;
        } 
        else { // Add if new
            Item item = new Item(id, price, list);
            tree.put(id, item);
            updateDesc(item);
            return 1;
        }
    }

    private void deleteDesc(Item item) { // Go through TreeSet in Map and remove the Items that link with that description
        for (Integer num : item.description) {
            TreeSet<Item> desc = map.get(num);
            if (desc != null) {
                desc.remove(item);
                if (desc.isEmpty() || desc == null){
                    map.remove(num);
                }
            }	
        }
    }


    private void updateDesc(Item item) { // Add Items to the description in TreeSet if it doesn't exist
        for (Integer num : item.description) {
            TreeSet<Item> desc = map.computeIfAbsent(num, k -> new TreeSet<>());
            desc.add(item);
        }
    }




    // b. Find(id): return price of item with given id (or 0, if not found).
    public int find(int id) {
    	return (tree.containsKey(id)) ? tree.get(id).price : 0;
    }

    /* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    public int delete(int id) { 
        if (!tree.containsKey(id)) {
            return 0;
        } 
        else { // Find ID first
            int total = 0;
            Item item = tree.get(id);
            for (Integer num : item.description) { // Go through TreeSet in Map to remove the Item that links to all its descriptions
                TreeSet<Item> desc = map.get(num);
                if (desc != null) {
                    desc.remove(item);
                    if (desc.isEmpty() || desc == null){
                        map.remove(num);
                    }
                }
                total += num; 
            }// Remove from Tree
            tree.remove(id);
            return total;
        }
    }


    /* 
       d. FindMinPrice(n): given an integer, find items whose description
       contains that number (exact match with one of the ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    public int findMinPrice(int n) {
        TreeSet<Item> disc = map.get(n);
        if(disc != null && !disc.isEmpty()) {
        	return disc.first().price; // First element in TreeSet if its ordered will always cost less
        	}
         return 0;
        }
    

    /* 
       e. FindMaxPrice(n): given an integer, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    
    public int findMaxPrice(int n) {
        TreeSet<Item> disc = map.get(n);
        if(disc != null && !disc.isEmpty()) {
        	return disc.last().price; // last element in TreeSet if its ordered will always cost more
        	}
         return 0;
        }
    
    /* 
       f. FindPriceRange(n,low,high): given int n, find the number
       of items whose description contains n, and in addition,
    */
    public int findPriceRange(int n, int low, int high) {
    	if(low > high) {
    		return 0;
    	}
    	TreeSet<Item> disc = map.get(n);
    	int total = 0;
    	if (disc == null) {
    		return 0;
    	}
    	for(Item item: disc) {// Look through all the prices of the TreeSet description to see if it lands between the range
    		if(item.price <= high && item.price >= low) {
    			total++;
    		}
    	}
    	return total;
    }

    /*
      g. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    
    public int removeNames(int id, List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Item item = tree.get(id);
        int sum = 0;
        for (int num : list) { // If the item contains the description, they we iterate through TreeSet description, and remove the description in the TreeSet for that Item
            if (item.description.contains(num)) {
                sum += num;
                TreeSet<Item> desc = map.get(num);
        		desc.remove(item);
        		if(desc.size() <= 1 || desc == null) {
        			map.remove(num, item);
        		}
            }
            item.description.remove(Integer.valueOf(num));
        }
        return sum;
    }
}