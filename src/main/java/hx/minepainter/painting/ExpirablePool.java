package hx.minepainter.painting;

import java.util.HashMap;
import java.util.Iterator;

public abstract class ExpirablePool<T,V> {
	
	final int expire;
	public ExpirablePool(int expire){
		this.expire = expire;
	}

	HashMap<T,Integer> timeouts = new HashMap<T,Integer>();
	HashMap<T,V> items = new HashMap<T,V>();
	
	boolean running = false;
	
	public void start(){
		running = true;
		new Thread(new Runnable(){
			@Override public void run(){
				while(running){
					for(Iterator<T> iter = timeouts.keySet().iterator(); iter.hasNext(); ){
						T t = iter.next();
						int count = timeouts.get(t); 
						if(count <= 0){
							iter.remove();
							release(items.remove(t));
						}else timeouts.put(t, count - 1);
					}
					
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(items.isEmpty())running = false;
				}
					
			}
		}).start();
	}
	
	public abstract void release(V v);
	
	public abstract V get();
	
	public void stop(){
		running = false;
	}
	
	public boolean contains(T t){
		return items.containsKey(t);
	}
	
	public V get(T t){
		if(!items.containsKey(t))
			items.put(t, get());
		timeouts.put(t, expire);
		return items.get(t);
	}
}
