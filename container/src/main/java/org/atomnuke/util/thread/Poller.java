package org.atomnuke.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Because I don't like Java Timer =/
 *
 * @author zinic
 */
public class Poller extends Thread {

   private static final Logger LOG = LoggerFactory.getLogger(Poller.class);

   private final Runnable task;
   private final long interval;

   private volatile boolean shouldContinue;

   public Poller(String name, Runnable task, long interval) {
      super(name);

      this.interval = interval;
      this.task = task;

      shouldContinue = true;
   }

   @Override
   public void run() {
      while (shouldContinue && !isInterrupted()) {
         try {
            task.run();

            // Lock on our monitor
            synchronized (this) {
               wait(interval);
            }
         } catch (InterruptedException ie) {
            LOG.warn("Poller " + getName() + " interrupted.");
            shouldContinue = false;
         }
      }
   }

   public synchronized void destroy() {
      shouldContinue = false;

      // Notify and interrupt the task thread
      notify();
      interrupt();
   }
}
