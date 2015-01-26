package hx.minepainter.painting;

import java.util.HashMap;
import java.util.Iterator;

public abstract class ExpirablePool<T, V> {

  final int expire;

  public ExpirablePool(int expire) {
    this.expire = expire;
  }

  HashMap<T, Integer> timeouts = new HashMap();
  HashMap<T, V> items = new HashMap();
  boolean running = false;

  public void start() {
    this.running = true;
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (ExpirablePool.this.running) {
          for (Iterator<T> iter = ExpirablePool.this.timeouts.keySet().iterator(); iter.hasNext();) {
            T t = iter.next();
            int count = ExpirablePool.this.timeouts.get(t);
            if (count <= 0) {
              iter.remove();
              ExpirablePool.this.release(ExpirablePool.this.items.remove(t));
            } else {
              ExpirablePool.this.timeouts.put(t, count - 1);
            }
          }
          try {
            Thread.sleep(80L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (ExpirablePool.this.items.isEmpty()) {
            ExpirablePool.this.running = false;
          }
        }
      }
    }).start();
  }

  public abstract void release(V paramV);

  public abstract V get();

  public void stop() {
    this.running = false;
  }

  public boolean contains(T t) {
    return this.items.containsKey(t);
  }

  public V get(T t) {
    if (!this.items.containsKey(t)) {
      this.items.put(t, get());
    }
    this.timeouts.put(t, this.expire);
    return this.items.get(t);
  }
}
