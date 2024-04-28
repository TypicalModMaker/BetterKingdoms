package dev.isnow.betterkingdoms.util.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class BetterCache<K, V> {
    private final LoadingCache<K, V> cache;
    private final ISaver<V> saver;

    @SuppressWarnings("all") // Unchecked cast & 'while' statement cannot complete without throwing an exception supress false positive
    public BetterCache(final String name, final ILoader<K, V> loader, final ISaver<V> saver, final IRemover<V> remover) {
        this.saver = saver;
        cache = Caffeine.newBuilder()
                .expireAfterAccess(300, TimeUnit.SECONDS)
                .removalListener((key, value, cause) -> {
                    if(value != null && !BetterKingdoms.getInstance().isShuttingDown()) {
                        if(cause == RemovalCause.EXPIRED || cause == RemovalCause.SIZE) {
                            saver.save((V) value);
                        } else if(cause == RemovalCause.EXPLICIT && remover != null) {
                            remover.remove((V) value);
                        }
                    }
                })
                .initialCapacity(BetterKingdoms.getInstance().getConfigManager().getMasterConfig().getCacheSizeLimit())
                .build(loader::load);

        BetterKingdoms.getInstance().getThreadPool().execute(() -> {
            while (true) {
                try {
                    cache.cleanUp();

                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    BetterLogger.warn("Failed to clean the cache: " + e + ", Cache type: " + name);
                }
            }
        });
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
