/**
 * This class represents a hash table.
 * 
 * @param <K> the type of the key
 * @param <V> the type of the value
 * 
 * @author Mehmet Ali Özdemir
 * @since 23.11.2023
 */
import java.util.LinkedList;
import java.util.List;

public class HashTable<K, V> {
    private static final int DEFAULT_SIZE = 101;
    private static final double LOAD_FACTOR = 0.75;

    private List<Items<K,V>>[] lists;
    private int currentSize;

    public HashTable() {
        this(DEFAULT_SIZE);
    }
    
    public HashTable(int size) {
        lists = new List[nextPrime(size)];

        for (int i = 0; i < lists.length; i++)
            lists[i] = new LinkedList<>();

        currentSize = 0;
    }


    /*
     * This method inserts an item to the hash table.
     * 
     * @param key the key of the item
     * @param value the value of the item
     */
    public void insert(K key, V value) {
        Items<K,V> newItem = new Items<>(key, value);
        List<Items<K,V>> list = lists[hash(key)];

        if (!list.contains(newItem)) {
            list.add(newItem);
            currentSize++;

            if (currentSize > lists.length * LOAD_FACTOR)
                rehash();
        }
    }


    /*
     * This method removes an item from the hash table.
     * 
     * @param key the key of the item
     */
    public void remove(K key) {
        List<Items<K,V>> list = lists[hash(key)];
        for (Items<K, V> item: list) {
            if (item.getKey().equals(key)) {
                list.remove(item);
                currentSize--;
                return;
            }
        }
    }


    /*
     * This method gets the value of the item with the given key.
     * @param key the key of the item
     * @return the value of the item
     */
    public V get(K key) {
        List<Items<K,V>> list = lists[hash(key)];
        for (Items<K,V> item: list) {
            if (item.getKey().equals(key))
                return item.getValue();
        }
        return null;
    }

    
    /*
     * This method checks if the hash table contains an item with the given key.
     * 
     * @param key the key of the item
     * @return true if the hash table contains the item, false otherwise
     */
    public boolean contains(K key) {
        List<Items<K,V>> list = lists[hash(key)];
        for (Items<K,V> item : list) {
            if (item.getKey().equals(key))
                return true;
        }
        return false;
    }


    /*
     * This method reconstructs the hash table with the size doubled.
     */
    private void rehash() {
        List<Items<K,V>>[] oldLists = lists;
        lists = new List[nextPrime(2 * oldLists.length)];

        for (int i = 0; i < lists.length; i++)
            lists[i] = new LinkedList<>();

        currentSize = 0;

        for (List<Items<K,V>> list : oldLists) {
            for (Items<K,V> item : list)
                insert(item.getKey(), item.getValue());
        }
    }


    /*
     * This method returns the hash value of the given key.
     * 
     * @param key the key of the item
     * @return the hash value of the key
     */
    private int hash(K key) {
        int hashValue = key.hashCode() % lists.length;

        if (hashValue < 0)
            hashValue += lists.length;

        return hashValue;
    } 


    /*
     * This method returns the next prime number after the given number.
     * 
     * @param currentPrime the number to find the next prime number after
     * @return the next prime number after the given number
     */
    private int nextPrime(int currentPrime) {
        if (currentPrime % 2 == 0)
            currentPrime++;
        
        while (!isPrime(currentPrime))
            currentPrime += 2;

        return currentPrime;
    }


    /*
     * This method checks if the given number is prime.
     * 
     * @param n the number to check
     * @return true if the given number is prime, false otherwise
     */
    private boolean isPrime(int n) {
        if (n == 2 || n == 3)
            return true;

        if (n == 1 || n % 2 == 0)
            return false;

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public int size() {
        return currentSize;
    }

    public List<Items<K,V>>[] getLists() {
        return lists;
    }
}