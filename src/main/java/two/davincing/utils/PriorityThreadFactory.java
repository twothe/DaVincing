package two.davincing.utils;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Two
 */
public class PriorityThreadFactory implements ThreadFactory, ForkJoinPool.ForkJoinWorkerThreadFactory {

  protected final ThreadGroup allThreads;
  protected final int priority;
  protected final AtomicInteger threadCount = new AtomicInteger(0);

  public PriorityThreadFactory(final String name, final int priority, final boolean daemon) {
    this.priority = priority;
    this.allThreads = new ThreadGroup(name);
    this.allThreads.setMaxPriority(priority);
    this.allThreads.setDaemon(daemon);
  }

  public PriorityThreadFactory(final int priority, final boolean daemon) {
    this("workers", priority, daemon);
  }

  public PriorityThreadFactory(final int priority) {
    this("workers", priority, false);
  }

  @Override
  public Thread newThread(final Runnable r) {
    final Thread result = new Thread(this.allThreads, r);
    result.setName(this.allThreads.getName() + " #" + threadCount.incrementAndGet());
    return result;
  }

  @Override
  public ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
    final ForkJoinWorkerThread result = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
    result.setPriority(priority);
    result.setName(this.allThreads.getName() + " #" + threadCount.incrementAndGet());
    return result;
  }
}
