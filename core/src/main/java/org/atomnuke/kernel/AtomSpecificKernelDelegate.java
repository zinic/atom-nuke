package org.atomnuke.kernel;

import java.util.concurrent.TimeUnit;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.impl.atom.AtomSpecificTaskManager;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class AtomSpecificKernelDelegate implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(AtomSpecificKernelDelegate.class);

   private static long ONE_MILLISECOND_IN_NANOS = 1000000;

   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutionManager executionManager;
   private final AtomSpecificTaskManager taskManager;

   private int drainMagnitude;

   public AtomSpecificKernelDelegate(AtomSpecificTaskManager taskManager, ExecutionManager executionManager) {
      this.crawlerCancellationRemote = new AtomicCancellationRemote();
      this.executionManager = executionManager;
      this.taskManager = taskManager;

      drainMagnitude = 1;
   }

   public AtomSpecificTaskManager taskManager() {
      return taskManager;
   }

   public CancellationRemote cancellationRemote() {
      return crawlerCancellationRemote;
   }

   private boolean shouldContinue() {
      final boolean executionManagerStopped = executionManager.state() == ExecutionManager.State.STOPPING || executionManager.state() == ExecutionManager.State.DESTROYED;
      final boolean canceled = crawlerCancellationRemote.canceled();

      return !canceled && !executionManagerStopped;
   }

   @Override
   public void run() {
      // Run until canceled
      while (shouldContinue()) {
         sleep(tick());
      }
   }

   private void sleep(TimeValue sleepTill) {
      final long totalNanoseconds = sleepTill.subtract(TimeValue.now()).value(TimeUnit.NANOSECONDS);

      try {
         if (totalNanoseconds > ONE_MILLISECOND_IN_NANOS) {
            millisecondGrainularSleep(totalNanoseconds);
         } else {
            microsecondGrainularSleep(totalNanoseconds);
         }
      } catch (InterruptedException ie) {
         LOG.warn("KernelDelegate interrupted. Shutting down right now.", ie);
         crawlerCancellationRemote.cancel();
      }
   }

   private void millisecondGrainularSleep(final long totalNanoseconds) throws InterruptedException {
      final long milliseconds = totalNanoseconds / ONE_MILLISECOND_IN_NANOS;
      final int nanoseconds = (int) (totalNanoseconds % ONE_MILLISECOND_IN_NANOS);

      Thread.sleep(milliseconds, nanoseconds);
   }

   private void microsecondGrainularSleep(final long totalNanoseconds) {
      long sleepTime = totalNanoseconds - System.nanoTime();

      do {
         final long then = System.nanoTime();

         Thread.yield();

         sleepTime -= System.nanoTime() - then;
      } while (sleepTime > 0);
   }

   private TimeValue tick() {
      // Sleep till the next polling time or for a couple of milliseconds
      if (executionManager.state() == ExecutionManager.State.DRAINING) {
         drainMagnitude += drainMagnitude == 1000 ? 0 : 1;

         // Compute the time we'll be yielding
         final long millisecondsToYield = 2 * drainMagnitude;

         // Log this since yielding is usually a bad thing
         LOG.error("Execution queue too large to continue polling. Yielding till " + millisecondsToYield + " to allow queue to drain.");

         return TimeValue.now().add(new TimeValue(millisecondsToYield, TimeUnit.MILLISECONDS));
      }

      drainMagnitude -= drainMagnitude == 0 ? 1 : 0;

      return taskManager.scheduleTasks();
   }
}