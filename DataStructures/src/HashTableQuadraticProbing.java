import java.util.*;

@SuppressWarnings("unchecked")//implements Iterable<K>
public class HashTableQuadraticProbing <K,V>  {

    private double loadFactor;
    private int capacity, threshold, modificationCount = 0;

    private int usedBucket = 0, keyCount = 0;

    private K[] keyTable;
    private V[] valueTable;

    private boolean containsFlag = false;

    private final K TOMBSTONE = (K) (new Object());

    private static final int DEFAULT_CAPACITY = 8;
    private static final double DEFAULT_LOAD_FACTOR = 0.45;

    public HashTableQuadraticProbing() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity, double loadFactor) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Illegal capacity " + capacity);
        if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor))
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        this.loadFactor = loadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, next2Power(capacity));
        threshold = (int) (this.capacity * this.loadFactor);

        keyTable = (K[]) new Object[this.capacity];
        valueTable = (V[]) new Object[this.capacity];
    }

    private static int next2Power(int n) {
        return Integer.highestOneBit(n) << 1;// hmmmm
    }

    private static int P(int x) {
        return (x * x + x) >> 1;
    }

    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            keyTable[i] = null;
            valueTable[i] = null;
        }
        keyCount = usedBucket = 0;
        modificationCount++;
    }

    public int size() {
        return keyCount;
    }

    public boolean isEmpty() {
        return keyCount == 0;
    }

    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    public V insert(K key, V val) {
        if (key == null) throw new IllegalArgumentException("Null key");
        if (usedBucket >= threshold) resizeTable();

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            if (keyTable[i] == TOMBSTONE) {
                if (j == -1) j = i;
            } else if (keyTable[i] != null) {
                if (keyTable[i].equals(key)) {
                    V oldValue = valueTable[i];
                    if (j == -1) {
                        valueTable[i] = val;
                    } else {
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                        keyTable[j] = key;
                        valueTable[j] = val;
                    }
                    modificationCount++;
                    return oldValue;
                }
            } else {
                if (j == -1) {
                    usedBucket++;
                    keyCount++;
                    keyTable[i] = key;
                    valueTable[i] = val;
                } else {
                    keyCount++;
                    keyTable[j] = key;
                    valueTable[j] = val;
                }

                modificationCount++;
                return null;
            }
            i = normalizeIndex(hash + P(x++));
        } while (true);
    }

    public boolean containsKey(K key) {
        return hasKey(key);
    }

    public boolean hasKey(K key) {
        // sets the containsFlag
        get(key);
        return containsFlag;
    }

    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Null Key");
        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            if (keyTable[i] == TOMBSTONE) {
                if (j == -1) j = i;
            } else if (keyTable[i] != null) {
                if (keyTable[i].equals(key)) {
                    containsFlag = true;

                    if (j != -1) {
                        keyTable[j] = keyTable[i];
                        valueTable[j] = valueTable[i];

                        keyTable[i] = TOMBSTONE;
                        valueTable[j] = null;

                        return valueTable[j];
                    } else {
                        return valueTable[i];
                    }
                }
            } else {
                containsFlag = false;
                return null;
            }

            i = normalizeIndex(hash + P(x++));
        } while (true);
    }

    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("Null key");

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, x = 1;

        for (; ; i = normalizeIndex(hash + P(x++))) {
            if (keyTable[i] == TOMBSTONE) continue;

            if (keyTable[i] == null) return null;

            if (keyTable.equals(key)) {
                keyCount--;
                modificationCount++;
                V oldValue = valueTable[i];
                keyTable[i] = TOMBSTONE;
                valueTable[i] = null;
                return oldValue;
            }
        }
    }

    private void resizeTable() {
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);

        K[] oldKeyTable = (K[]) new Object[capacity];
        V[] oldValueTable = (V[]) new Object[capacity];

        K[] keyTabletmp = keyTable;
        keyTable = oldKeyTable;
        oldKeyTable = keyTabletmp;

        V[] valueTableTmp = valueTable;
        valueTable = oldValueTable;
        oldValueTable = valueTableTmp;

        keyCount = usedBucket = 0;

        for (int i = 0; i < oldKeyTable.length; i++) {
            if (oldKeyTable[i] != null && oldKeyTable[i] != TOMBSTONE)
                insert(oldKeyTable[i], oldValueTable[i]);
            oldKeyTable[i] = null;
            oldValueTable[i] = null;
        }
    }
};
    // Return a String view of this hash-table.
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("{");
//        for (int i = 0; i < capacity; i++)
//            if (keys[i] != null && keys[i] != TOMBSTONE) sb.append(keys[i] + " => " + values[i] + ", ");
//        sb.append("}");
//
//        return sb.toString();
//    }
//
//    @Override
//    public Iterator<K> iterator() {
//        // Before the iteration begins record the number of modifications
//        // done to the hash-table. This value should not change as we iterate
//        // otherwise a concurrent modification has occurred :0
//        final int MODIFICATION_COUNT = modificationCount;

//        return new Iterator<K>() {
//            int index, keysLeft = keyCount;
//
//            @Override
//            public boolean hasNext() {
//                // The contents of the table have been altered
//                if (MODIFICATION_COUNT != modificationCount) throw new ConcurrentModificationException();
//                return keysLeft != 0;
//            }

            // Find the next element and return it
//            @Override
//            public K next() {
//                while (keys[index] == null || keys[index] == TOMBSTONE) index++;
//                keysLeft--;
//                return keys[index++];
//            }
//
//            @Override
//            public void remove() {
//                throw new UnsupportedOperationException();
//            }
//        };


    //}


