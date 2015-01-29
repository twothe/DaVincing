package two.davincing.painting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import two.davincing.DaVincing;

public abstract class ExpirablePool<T, V> implements Runnable {

  static final long DEFAULT_CHECK_INTERVAL_MS = 80;
  static final long DEFAULT_EXPIRY_TIME_MS = DEFAULT_CHECK_INTERVAL_MS * 12;

  protected final class MapEntry<V> implements Comparable<Long> {

    final V value;
    long expireDate = 0;

    public MapEntry(final V value) {
      this.value = value;
    }

    public void updateExpiry() {
      this.expireDate = System.currentTimeMillis() + DEFAULT_EXPIRY_TIME_MS;
    }

    @Override
    public int compareTo(final Long o) {
      return Long.compare(expireDate, o);
    }
  }

  protected final ConcurrentHashMap<T, MapEntry<V>> items = new ConcurrentHashMap<T, MapEntry<V>>();
  protected ScheduledFuture<?> cleanupTask = null;

  public ExpirablePool() {
  }

  protected abstract void release(V v);

  protected abstract V create();

  public void start() {
    if (cleanupTask == null) {
      cleanupTask = DaVincing.backgroundTasks.scheduleAtFixedRate(this, 0, DEFAULT_CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }
  }

  public void stop() {
    if (cleanupTask != null) {
      cleanupTask.cancel(false);
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

  public boolean contains(T t) {
    if (this.cleanupTask == null) {
      throw new IllegalStateException("ExpirablePool has not been started before first use");
    }
    return items.containsKey(t);
  }

  public V get(T t) {
    if (this.cleanupTask == null) {
      throw new IllegalStateException("ExpirablePool has not been started before first use");
    }
    final MapEntry<V> result = items.computeIfAbsent(t, addEntry);
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

  protected final Function<T, MapEntry<V>> addEntry = new Function<T, MapEntry<V>>() {

    @Override
    public MapEntry<V> apply(T t) {
      final V value = create();
      return new MapEntry<V>(value);
    }
  };

}
