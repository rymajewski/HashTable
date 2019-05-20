import java.util.ArrayList;
/**
 * A hash table for K key, V value pair, using an ArrayList of linked nodes utilizing chained buckets.
 * @author ryant
 *
 * @param <K>
 * @param <V>
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
	
	private ArrayList<hashNode<K,V>> hashBucket; // ArrayList of type hashNode
	private int initialCapacity; // The capacity of the hash table, both initial and when the table rehashes
	private double loadFactorThreshold; // The load factor for the hash table
	private int numKeys; // Number of keys within the list
		
	/**
	 * Default no-arg constructor for the hash table
	 * Sets each of the variables to a predetermined value
	 * Creates a new ArrayList setting the nodes to null to have an empty list
	 */
	public HashTable() {
		hashBucket = new ArrayList<>();
		initialCapacity = 11; // Set the initial capacity to an odd number, helps with the hashcode
		loadFactorThreshold = 0.75;
		numKeys = 0;
		for (int i = 0; i < initialCapacity; i++) { // Sets each index of the ArrayList to null to start, empty list
			hashBucket.add(null);
		}
	}
	
	/**
	 * Constructor that takes two values
	 * Assigns the appropriate variable to the value that was passed to the constructor
	 * Creates a new ArrayList and sets each node to null
	 * @param initialCapacity
	 * @param loadFactorThreshold
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		this.initialCapacity = initialCapacity;
		this.loadFactorThreshold = loadFactorThreshold;
		numKeys = 0;
		hashBucket = new ArrayList<>();
		for (int i = 0; i < initialCapacity; i++) // Empty list 
			hashBucket.add(null);
	}
	
	/**
	 * This method inserts key, value pair into the appropriate index of the List based on the hashcode % capacity of the table.
	 * @param key The key of type K to be inserted into the hashtable 
	 * @param value The value associated with the key that is being inserted
	 */
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		if (key == null) // The key can't be null, will throw null key exception
			throw new IllegalNullKeyException();
		int index = getHashIndex(key); // Uses the private method that gets the hashcode of the key and then gets the index based on that
		if (hashBucket.get(index) != null) // If there is an key already at the index then a duplicate key exception should be thrown
			throw new DuplicateKeyException();
		hashNode<K,V> head = hashBucket.get(index); // Find the head for the given key within the chain
		
		while(head != null) {
			if (head.key.equals(key)) { // Checks to see if the key already exists within the list.
				head.value = value;
				return; 
			}
			head = head.next;
		}
		
		numKeys++; // Increase the number of keys in the hash table
		head = hashBucket.get(index); // Go to the head of the cahin
		hashNode<K,V> node = new hashNode<K,V>(key, value); // node is the new node with the key, value pair
		node.next = head;
		hashBucket.set(index, node);
		
		// Check to see if the current load factor is greater than or equal to the threshold
		if ((1.0 * numKeys) / initialCapacity >= loadFactorThreshold) { 
			ArrayList<hashNode<K,V>> temp = hashBucket; // Create temp ArrayList = to original
			hashBucket = new ArrayList<>(); // Set the original to a new ArrayList
			initialCapacity = (2 * initialCapacity) + 1; // Update the capacity of the ArrayList
			numKeys = 0; // Set numKeys to 0 for the new ArrayList
			for (int i = 0; i < initialCapacity; i++) // Set each of the nodes to empty in the new ArrayList
				hashBucket.add(null);
			for (hashNode<K,V> headNode : temp) { // For each loop
				while (headNode != null) {
					insert(headNode.key, headNode.value); // Call the insert method for key, value pair
					headNode = headNode.next; // Move to the next node in the hash table
				}
			}
		}	
	}
	
	/**
	 * This method removes a key, value pair for a given key
	 * @param key The key of the key, value pair that you want to remove.
	 * @return boolean true or false on whether the pair was removed or not
	 */
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) // Key can't equal null, will throw an exception
			throw new IllegalNullKeyException();
		
		int index = getHashIndex(key); // Get the index of the key that was passed to the remove method
		hashNode<K,V> head = hashBucket.get(index); // Create a head node and equal it to the index
		hashNode<K,V> prev = null;
		while (head != null) {
			if (head.key.equals(key)) // The key has been found
				break;
			prev = head; // Progress to the next node for prev
			head = head.next; // Set head to the next node in the hash table
		}
		
		if (head == null) // The key was not found
			return false;
		
		numKeys--; // Decrease the counter for the number of keys
		
		if (prev != null) // This removes the key from the hash table
			prev.next = head.next; 
		else 
			hashBucket.set(index, head.next);
		return true; // Remove was successful
	}
	
	/**
	 * This method return the index of the key that was passed to the method
	 * @param key The key in which you wan thte index to be found
	 * @return int the index of the key
	 */
	private int getHashIndex(K key) {
		int hash = key.hashCode(); // Get the hash code of the given key
		int index = hash % initialCapacity; // Get the index of where the hash code should be in the list
		return index;
	}
	
	/**
	 * This method returns the value of a key
	 * @param key of the pair where you want the value
	 * @return value in the key, value pair
	 */
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) // Key can't be null, will throw an exception.
			throw new IllegalNullKeyException();
		int index = getHashIndex(key); // The index of the given key
		hashNode<K,V> head = hashBucket.get(index); // The start of the list
		
		while(head != null) {
			if (head.key.equals(key)) // See in the key of the head node and key that was passed are equal
				return head.value; // Found and return value
			else if (head.next == null) { // Key hasn't been found within the hash table
				return null;
			}
			head = head.next; 
		}
		throw new KeyNotFoundException();
	}
	
	/**
	 * Getter method for the numKeys variable
	 * @returns int of the numKeys
	 */
	@Override
	public int numKeys() {
		return numKeys;
	}
	
	/**
	 * Getter method for the LoadFactorThreshold variable
	 * @returns double Load factor threshold
	 */
	@Override
	public double getLoadFactorThreshold() {
		return loadFactorThreshold;
	}
	
	/**
	 * Method calculates the current load factor of the hash table
	 * @returns double Load factor
	 */
	@Override
	public double getLoadFactor() {
		return (1.0 * numKeys) / initialCapacity;
	}
	
	/**
	 * Getter method for the initialCapacity variable
	 * @returns int the capacity or size of the table
	 */
	@Override
	public int getCapacity() {
		return initialCapacity;
	}
	
	/**
	 * Method gives the variable of the Collision resolution that I'm doing
	 * I'm using chained buckets method, array of linked nodes
	 * @returns int
	 */
	@Override
	public int getCollisionResolution() { 
		return 5;
	}
	
	/**
	 * Inner hash node class, node to hold the key, value pair, and a reference of the next node
	 * @author ryant
	 *
	 * @param <K>
	 * @param <V>
	 */
	private class hashNode<K, V> {
		K key;
		V value;
		hashNode<K,V> next;
		
		/**
		 * Constructor assigns key and value variables to values passed to the method
		 * @param key
		 * @param value
		 */
		private hashNode(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}		
}