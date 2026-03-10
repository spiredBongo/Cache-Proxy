package org.example.cacheproxy.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCacheService implements CacheService{

    private static final class Entry{
        private final CachedResponse cachedResponse;
        private final Instant expiresAt;

        private Entry(CachedResponse cachedResponse, Instant expiresAt){
            this.cachedResponse = cachedResponse;
            this.expiresAt = expiresAt;
        }
    }

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public Optional<CachedResponse> get(String key) {
        Entry entry = store.get(key);

        if(entry == null) {
            return Optional.empty();
        }

        if(Instant.now().isAfter(entry.expiresAt)){
            store.remove(key);
            return Optional.empty();
        }

        return Optional.of(entry.cachedResponse);

    }

    @Override
    public void put(String key, CachedResponse response, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            store.remove(key);
            return;
        }
        store.put(key, new Entry(response, Instant.now().plus(ttl)));
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }
}
