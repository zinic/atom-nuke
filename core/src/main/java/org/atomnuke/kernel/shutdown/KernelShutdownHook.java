package org.atomnuke.kernel.shutdown;

import java.util.Stack;
import org.atomnuke.lifecycle.Reclaimable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class KernelShutdownHook implements Runnable, ShutdownHook {

   private static final Logger LOG = LoggerFactory.getLogger(KernelShutdownHook.class);

   private final Stack<Reclaimable> childHooks;
   private boolean shutdown;

   public KernelShutdownHook() {
      childHooks = new Stack<Reclaimable>();
      shutdown = false;

      registerWithRuntime();
   }

   private void registerWithRuntime() {
      Runtime.getRuntime().addShutdownHook(new Thread(this));
   }

   @Override
   public synchronized void enlist(Reclaimable destroyable) {
      if (!shutdown) {
         childHooks.push(destroyable);
      }
   }

   @Override
   public synchronized void run() {
      shutdown();
   }

   @Override
   public void shutdown() {
      if (!shutdown) {
         shutdown = true;

         LOG.info("Process shutting down.");
         callHooks();
      }
   }

   private void callHooks() {
      while (!childHooks.isEmpty()) {
         final Reclaimable shutdownHook = childHooks.pop();

         try {
            LOG.info("Running destroy hook, " + shutdownHook + ".");

            shutdownHook.destroy();
         } catch (Exception ex) {
            LOG.info("Failure occured while calling destroy hook, " + shutdownHook + ". Reason: " + ex.getMessage(), ex);
         }
      }
   }
}
