/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Set;
/*  6:   */ 
/*  7:   */ public abstract class ExpirablePool<T, V>
/*  8:   */ {
/*  9:   */   final int expire;
/* 10:   */   
/* 11:   */   public ExpirablePool(int expire)
/* 12:   */   {
/* 13:10 */     this.expire = expire;
/* 14:   */   }
/* 15:   */   
/* 16:13 */   HashMap<T, Integer> timeouts = new HashMap();
/* 17:14 */   HashMap<T, V> items = new HashMap();
/* 18:16 */   boolean running = false;
/* 19:   */   
/* 20:   */   public void start()
/* 21:   */   {
/* 22:19 */     this.running = true;
/* 23:20 */     new Thread(new Runnable()
/* 24:   */     {
/* 25:   */       public void run()
/* 26:   */       {
/* 27:22 */         while (ExpirablePool.this.running)
/* 28:   */         {
/* 29:23 */           for (Iterator<T> iter = ExpirablePool.this.timeouts.keySet().iterator(); iter.hasNext();)
/* 30:   */           {
/* 31:24 */             T t = iter.next();
/* 32:25 */             int count = ((Integer)ExpirablePool.this.timeouts.get(t)).intValue();
/* 33:26 */             if (count <= 0)
/* 34:   */             {
/* 35:27 */               iter.remove();
/* 36:28 */               ExpirablePool.this.release(ExpirablePool.this.items.remove(t));
/* 37:   */             }
/* 38:   */             else
/* 39:   */             {
/* 40:29 */               ExpirablePool.this.timeouts.put(t, Integer.valueOf(count - 1));
/* 41:   */             }
/* 42:   */           }
/* 43:   */           try
/* 44:   */           {
/* 45:33 */             Thread.sleep(80L);
/* 46:   */           }
/* 47:   */           catch (InterruptedException e)
/* 48:   */           {
/* 49:35 */             e.printStackTrace();
/* 50:   */           }
/* 51:37 */           if (ExpirablePool.this.items.isEmpty()) {
/* 52:37 */             ExpirablePool.this.running = false;
/* 53:   */           }
/* 54:   */         }
/* 55:   */       }
/* 56:   */     }).start();
/* 57:   */   }
/* 58:   */   
/* 59:   */   public abstract void release(V paramV);
/* 60:   */   
/* 61:   */   public abstract V get();
/* 62:   */   
/* 63:   */   public void stop()
/* 64:   */   {
/* 65:49 */     this.running = false;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public boolean contains(T t)
/* 69:   */   {
/* 70:53 */     return this.items.containsKey(t);
/* 71:   */   }
/* 72:   */   
/* 73:   */   public V get(T t)
/* 74:   */   {
/* 75:57 */     if (!this.items.containsKey(t)) {
/* 76:58 */       this.items.put(t, get());
/* 77:   */     }
/* 78:59 */     this.timeouts.put(t, Integer.valueOf(this.expire));
/* 79:60 */     return this.items.get(t);
/* 80:   */   }
/* 81:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.ExpirablePool
 * JD-Core Version:    0.7.0.1
 */