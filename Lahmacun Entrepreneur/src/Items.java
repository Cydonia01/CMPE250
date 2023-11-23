/**
 * This class represents an item in the hash table.
 * 
 * @param <K> the type of the key
 * @param <V> the type of the value
 * 
 * @author Mehmet Ali Özdemir
 * @since 23.11.2023
 */
public class Items<K, V> {
    private final K key;
    private V value;
    
    public Items(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}