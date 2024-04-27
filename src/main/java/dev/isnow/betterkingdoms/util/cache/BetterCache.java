package dev.isnow.betterkingdoms.util.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.database.DatabaseManager;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;

public class BetterCache<K, V> {
    private final LoadingCache<K, V> cache;
    private final ISaver<V> saver;

    @SuppressWarnings("unchecked")
    public BetterCache(final ILoader<K, V> loader, final ISaver<V> saver, final IRemover<V> remover) {
        this.saver = saver;
        cache = Caffeine.newBuilder()
                .expireAfterAccess(300, TimeUnit.SECONDS)
                .removalListener((key, value, cause) -> {
                    if(value != null) {
                        if(cause == RemovalCause.EXPIRED) {
                            saver.save((V) value);
                        } else if(cause == RemovalCause.EXPLICIT && remover != null) {
                            remover.remove((V) value);
                        }
                    }
                    if(cause == RemovalCause.EXPIRED && value != null) {
                        saver.save((V) value);
                    }
                })
                .initialCapacity(BetterKingdoms.getInstance().getConfigManager().getMasterConfig().getCacheSizeLimit())
                .build(loader::load);
    }

    public V get(final K key) {
        return get(key, true);
    }

    public V get(final K key, final boolean runLoader) {
        if(runLoader) {
            return cache.get(key);
        } else {
            return cache.getIfPresent(key);
        }
    }


    public void remove(final K key) {
        cache.invalidate(key);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void preload(final K key) {
        cache.get(key);
    }

    public boolean contains(final K key) {
        return cache.getIfPresent(key) != null;
    }

    public void put(final K key, final V value) {
        cache.put(key, value);
    }

    public void save(final V value) {
        saver.save(value);
    }

    public Collection<V> getAll() {
        return cache.asMap().values();
    }
}
