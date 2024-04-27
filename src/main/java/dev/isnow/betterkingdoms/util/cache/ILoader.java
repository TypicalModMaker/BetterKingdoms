package dev.isnow.betterkingdoms.util.cache;
public interface ILoader<K, V> {
    V load(K key);
}
