package org.atomnuke.kernel;

import java.util.concurrent.TimeUnit;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class KernelDelegate implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(KernelDelegate.class);
   private static long ONE_MILLISECOND_IN_NANOS = 1000000;
   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutionManager executionManager;
   private final TaskManager taskManager;
   private int drainMagnitude;

   public KernelDelegate(CancellationRemote crawlerCancellationRemote, ExecutionManager executionManager, TaskManager taskManager) {
      this.crawlerCancellationRemote = crawlerCancellationRemote;
      this.executionManager = executionManager;
      this.taskManager = taskManager;

      drainMagnitude = 1;
   }

   @Override
   public void run() {
      // Run until canceled
      while (!crawlerCancellationRemote.canceled()) {
         final TimeValue now = TimeValue.now();
         TimeValue sleepTime;

         // Sleep till the next polling time or for a couple of milliseconds
         if (executionManager.draining()) {
            drainMagnitude += drainMagnitude == 1000 ? 0 : 1;

            sleepTime = new TimeValue(2 * drainMagnitude, TimeUnit.MILLISECONDS);

            LOG.error("Execution queue too large to continue polling. Yielding " + sleepTime.value() + " millisecond to allow queue to drain.");
         } else {
            drainMagnitude -= drainMagnitude == 0 ? 1 : 0;

            sleepTime = taskManager.scheduleTasks();
         }

         if (sleepTime.isGreaterThan(TimeValue.zero())) {
            try {
               long sleepNanos = sleepTime.subtract(now).value(TimeUnit.NANOSECONDS);

               while (sleepNanos > 0) {
                  final long then = System.nanoTime();

                  // Sleep if there's nothing to poll at the moment
                  if (sleepNanos > ONE_MILLISECOND_IN_NANOS) {
                     Thread.sleep(sleepNanos / ONE_MILLISECOND_IN_NANOS);
                  } else {
                     // Yield if we're not going to sleep
                     Thread.yield();
                  }

                  sleepNanos -= System.nanoTime() - then;
               }
            } catch (InterruptedException ie) {
               LOG.warn("KernelDelegate interrupted. Shutting down right now.", ie);
               break;
            }
         }
      }
   }
}
