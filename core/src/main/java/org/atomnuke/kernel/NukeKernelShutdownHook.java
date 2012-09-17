package org.atomnuke.kernel;

import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeKernelShutdownHook implements Runnable, KernelShutdownHook {

   private static final Logger LOG = LoggerFactory.getLogger(NukeKernelShutdownHook.class);
   private final Stack<Runnable> childHooks;
   private boolean shutdown;

   public NukeKernelShutdownHook() {
      childHooks = new Stack<Runnable>();
      shutdown = false;

      registerWithRuntime();
   }

   private void registerWithRuntime() {
      Runtime.getRuntime().addShutdownHook(new Thread(this));
   }

   @Override
   public synchronized void enlistShutdownHook(Runnable r) {
      if (!shutdown) {
         childHooks.push(r);
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
         final Runnable shutdownHook = childHooks.pop();

         try {
            LOG.info("Running destroy hook, " + shutdownHook + ".");

            shutdownHook.run();
         } catch (Exception ex) {
            LOG.info("Failure occured while calling destroy hook, " + shutdownHook + ". Reason: " + ex.getMessage(), ex);
         }
      }
   }
}
