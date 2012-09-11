package org.atomnuke.kernel;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeRejectionHandler implements RejectedExecutionHandler {

   private static final Logger LOG = LoggerFactory.getLogger(NukeRejectionHandler.class);

   @Override
   public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      if (executor.isShutdown()) {
         // Ingore shutdown
         LOG.debug("Runnable(" + r + ") rejected for execution: pool is shutting down.");
         return;
      }

      LOG.info("Runnable(" + r + ") rejected for execution.");
   }
}
