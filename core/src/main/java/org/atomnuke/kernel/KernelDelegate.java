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
         TimeValue sleepTill;

         // Sleep till the next polling time or for a couple of milliseconds
         if (executionManager.draining()) {
            drainMagnitude += drainMagnitude == 1000 ? 0 : 1;

            sleepTill = TimeValue.now().add(new TimeValue(2 * drainMagnitude, TimeUnit.MILLISECONDS));

            LOG.error("Execution queue too large to continue polling. Yielding till " + sleepTill.value(TimeUnit.MILLISECONDS) + " to allow queue to drain.");
         } else {
            drainMagnitude -= drainMagnitude == 0 ? 1 : 0;

            sleepTill = taskManager.scheduleTasks();
         }

         sleep(sleepTill);
      }
   }

   private void sleep(TimeValue sleepTill) {
      final long totalNanoseconds = sleepTill.subtract(TimeValue.now()).value(TimeUnit.NANOSECONDS);

      try {
         if (totalNanoseconds > ONE_MILLISECOND_IN_NANOS) {
            final long milliseconds = totalNanoseconds / ONE_MILLISECOND_IN_NANOS;
            final int nanoseconds = (int) (totalNanoseconds % ONE_MILLISECOND_IN_NANOS);

            Thread.sleep(milliseconds, nanoseconds);
         } else {
            long sleepTime = totalNanoseconds - System.nanoTime();

            do {
               final long then = System.nanoTime();

               Thread.yield();

               sleepTime -= System.nanoTime() - then;
            } while (sleepTime > 0);
         }
      } catch (InterruptedException ie) {
         LOG.warn("KernelDelegate interrupted. Shutting down right now.", ie);
         crawlerCancellationRemote.cancel();
      }
   }
}
