package org.atomnuke.task.impl;

import java.util.UUID;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TaskHandleImpl implements TaskHandle {

   private final CancellationRemote cancelationRemote;
   private final TimeValue interval;
   private final UUID taskId;

   public TaskHandleImpl(UUID taskId, TimeValue interval) {
      this.interval = interval;
      this.taskId = taskId;

      this.cancelationRemote = new AtomicCancellationRemote();
   }

   @Override
   public TimeValue scheduleInterval() {
      return interval;
   }

   @Override
   public UUID id() {
      return taskId;
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return cancelationRemote;
   }
}
