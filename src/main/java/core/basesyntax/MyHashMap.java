package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int MULTIPLIER_TABLE_GROW = 2;
    private Node<K, V>[] table;
    private int size;
    private int actualCapacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = (int) (actualCapacity * DEFAULT_LOAD_FACTOR);

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkResize();
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            currentNode = new Node<>(key, value, null);
            table[index] = currentNode;
            size++;
        } else {
            while (currentNode != null) {
                if (isKeyValid(key, currentNode)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<K, V>(
                            key, value,null
                    );
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
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
        return hash & (actualCapacity - 1);
    }

    private int calculateHash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private void checkResize() {
        if (size >= threshold) {
            actualCapacity *= MULTIPLIER_TABLE_GROW;
            threshold = (int) (actualCapacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] old = table;
            table = new Node[actualCapacity];
            for (Node<K, V> node : old) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    int newIndex = calculateIndex(node.key);
                    node.next = table[newIndex];
                    table[newIndex] = node;
                    node = nextNode;
                }
            }
        }
    }

    private boolean isKeyValid(K key, Node<K, V> node) {
        return (node.key == key || (node.key != null && node.key.equals(key)));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.next = next;
            this.key = key;
        }
    }
}
