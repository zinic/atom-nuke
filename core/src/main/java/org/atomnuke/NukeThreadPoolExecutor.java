package org.atomnuke;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeThreadPoolExecutor extends ThreadPoolExecutor {

   private static final Logger LOG = LoggerFactory.getLogger(NukeThreadPoolExecutor.class);

   public NukeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
   }

   @Override
   protected void afterExecute(Runnable r, Throwable t) {
      if (t != null) {
         LOG.error(t.getMessage(), t);
      }
   }
}
