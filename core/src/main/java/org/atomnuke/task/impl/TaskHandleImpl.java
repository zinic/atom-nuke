package org.atomnuke.task.impl;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TaskHandleImpl implements TaskHandle {

   private final static AtomicLong TASK_COUNT = new AtomicLong(1);

   private final CancellationRemote cancelationRemote;
   private final boolean reenterant;
   private final long taskId;

   public TaskHandleImpl(boolean reenterant, CancellationRemote cancelationRemote) {
      this.reenterant = reenterant;
      this.cancelationRemote = cancelationRemote;
      this.taskId = TASK_COUNT.incrementAndGet();
   }

   @Override
   public boolean reenterant() {
      return reenterant;
   }

   @Override
   public long id() {
      return taskId;
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return cancelationRemote;
   }
}
