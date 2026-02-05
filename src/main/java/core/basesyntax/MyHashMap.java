package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int MULTIPLIER_TABLE_GROW = 2;
    private static final int ZERO = 0;
    private Node<K, V>[] table;
    private int size;
    private int acutalcapacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = (int) (acutalcapacity * DEFAULT_LOAD_FACTOR);

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            acutalcapacity = DEFAULT_INITIAL_CAPACITY;
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            table[calculateIndex(key)] = new Node<>(key, value, calculateHash(key), null);
            size++;
        } else {
            checkResize();
            int index = calculateIndex(key);
            Node<K,V> currentNode = table[index];

            if (currentNode == null) {
                currentNode = new Node<>(key, value, calculateHash(key), null);
                table[index] = currentNode;
                size++;
            } else {
                while (currentNode != null) {
                    if (isKeyValid(key, currentNode)) {
                        currentNode.value = value;
                        return;
                    }
                    if (currentNode.next == null) {
                        currentNode.next = new Node<K, V>(key, value, calculateHash(key), null);
                        size++;
                        return;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        Node<K,V> currentNode = table[calculateIndex(key)];
        while (currentNode != null) {
            if (isKeyValid(key, currentNode)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = calculateHash(key);
        return hash & (acutalcapacity - 1);
    }

    private int calculateHash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private void checkResize() {
        if (size >= threshold) {
            acutalcapacity *= MULTIPLIER_TABLE_GROW;
            threshold = (int) (acutalcapacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] old = table;
            table = new Node[acutalcapacity];
            size = ZERO;
            for (Node<K, V> node : old) {
                while (node != null) {

                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean isKeyValid(K key, Node<K, V> node) {
        if (node.key == null && key == null) {
            return true;
        }
        if (node.key == null || key == null) {
            return false;
        }
        return key.equals(node.key);
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.value = value;
            this.next = next;
            this.key = key;
            this.hash = hash;
        }
    }
}
