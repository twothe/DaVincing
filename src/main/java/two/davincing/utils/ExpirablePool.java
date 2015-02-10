package two.davincing.utils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import two.davincing.DaVincing;

public abstract class ExpirablePool<K, V> implements Runnable {

  static final long DEFAULT_CHECK_INTERVAL_MS = 80;
  static final long DEFAULT_EXPIRY_TIME_MS = 60 * 1000;

  protected final class MapEntry<V> {

    final V value;
    long expireDate = 0;

    public MapEntry(final V value) {
      this.value = value;
    }

    public void updateExpiry() {
      this.expireDate = System.currentTimeMillis() + DEFAULT_EXPIRY_TIME_MS;
    }
  }

  protected final ConcurrentHashMap<K, MapEntry<V>> items = new ConcurrentHashMap<K, MapEntry<V>>();
  protected ScheduledFuture<?> cleanupTask = null;

  public ExpirablePool() {
  }

  protected abstract void release(final V value);

  protected abstract V create(final K key);

  public void start() {
    if (cleanupTask == null) {
      cleanupTask = DaVincing.backgroundTasks.scheduleAtFixedRate(this, 0, DEFAULT_CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }
  }

  public void stop() {
    if (cleanupTask != null) {
      cleanupTask.cancel(false);
      cleanupTask = null;
    }
    clear();
  }

  @Override
  public void run() {
    removeAllExpired(System.currentTimeMillis());
  }

  public void clear() {
    removeAllExpired(Long.MAX_VALUE);
  }

  public boolean contains(K key) {
    if (this.cleanupTask == null) {
      throw new IllegalStateException("ExpirablePool has not been started before first use");
    }
    return items.containsKey(key);
  }

  public V get(K key) {
    if (this.cleanupTask == null) {
      throw new IllegalStateException("ExpirablePool has not been started before first use");
    }
    MapEntry<V> result = items.get(key);
    if (result == null) {
      result = new MapEntry<V>(create(key));
      final MapEntry<V> oldValue = items.putIfAbsent(key, result);
      if (oldValue != null) {
        result = oldValue;
      }
    }
    result.updateExpiry();
    return result.value;
  }

  protected void removeAllExpired(final long expiryDate) {
    try {
      for (final Iterator<MapEntry<V>> it = items.values().iterator(); it.hasNext();) {
        final MapEntry<V> entry = it.next();
        if (expiryDate > entry.expireDate) {
          it.remove();
          release(entry.value);
        }
      }
    } catch (Throwable t) {
      DaVincing.log.error("[ExpirablePool]: ", t);
    }
  }

}
