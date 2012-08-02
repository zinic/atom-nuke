package net.jps.nuke;

import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.task.ManagedTask;
import net.jps.nuke.task.submission.TaskManager;
import net.jps.nuke.threading.ExecutionManager;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class KernelDelegate implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(KernelDelegate.class);
   private static final TimeValue ZERO_NANOSECONDS = new TimeValue(0, TimeUnit.NANOSECONDS);
   
   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutionManager executionManager;
   private final TaskManager taskManager;

   public KernelDelegate(CancellationRemote crawlerCancellationRemote, TaskManager taskManager, ExecutionManager executionManager) {
      this.crawlerCancellationRemote = crawlerCancellationRemote;
      this.executionManager = executionManager;
      this.taskManager = taskManager;
   }

   public synchronized void wake() {
      notify();
   }

   @Override
   public void run() {
      // Run until canceled
      while (!crawlerCancellationRemote.canceled()) {
         final TimeValue now = TimeValue.now();
         final TimeValue closestPollTime = scheduleTasks(taskManager.tasks(), now);

         // Sleep till the next polling time or for a couple of milliseconds
         final TimeValue sleepTime = closestPollTime != null ? now.subtract(closestPollTime) : ZERO_NANOSECONDS;

         try {
            // Sleep if there's nothing to poll at the moment
            if (sleepTime.value() > 0) {
               sleepTime.sleep();
            } else {
               // Yield if we're not going to sleep
               Thread.yield();
            }
         } catch (InterruptedException ie) {
            LOG.warn("KernelDelegate interrupted. Shutting down right now.", ie);
            break;
         }
      }
   }

   private TimeValue scheduleTasks(final List<ManagedTask> tasks, TimeValue now) {
      TimeValue closestPollTime = null;

      for (ManagedTask managedTask : tasks) {
         final TimeValue nextPollTime = managedTask.nextPollTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime)) {

            // Reentrant tasks are always eligible to run if their next polling
            // time has arrived.
            if (managedTask.isReentrant() || !executionManager.submitted(managedTask)) {
               executionManager.submit(managedTask);
            }
         } else if (closestPollTime == null || closestPollTime.isGreaterThan(nextPollTime)) {
            // If the closest polling time is null or later than this task's
            // next polling time, it becomes the next time the kernel wakes

            closestPollTime = nextPollTime;
         }
      }

      return closestPollTime;
   }
}
