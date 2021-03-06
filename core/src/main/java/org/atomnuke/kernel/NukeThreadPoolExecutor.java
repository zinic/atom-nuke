package org.atomnuke.kernel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.atomnuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeThreadPoolExecutor extends ThreadPoolExecutor {

   private static final Logger LOG = LoggerFactory.getLogger(NukeThreadPoolExecutor.class);

   public NukeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, TimeValue threadKeepAliveInterval, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      super(corePoolSize, maximumPoolSize, threadKeepAliveInterval.value(), threadKeepAliveInterval.unit(), workQueue, threadFactory, handler);
   }

   @Override
   protected void afterExecute(Runnable r, Throwable t) {
      if (t != null) {
         LOG.error(t.getMessage(), t);
      }
   }
}
