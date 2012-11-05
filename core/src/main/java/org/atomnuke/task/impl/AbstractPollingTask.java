package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.task.PollingTaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public abstract class AbstractPollingTask extends EnvironmentAwareManagedTask<PollingTaskHandle> {

   private TimeValue timestamp;

   public AbstractPollingTask(InstanceContext<Runnable> runnableCtx, PollingTaskHandle taskHandle) {
      super(runnableCtx, taskHandle);

      timestamp = TimeValue.now();
   }

   @Override
   public final void scheduleNext() {
      timestamp = TimeValue.now();
   }

   @Override
   public final TimeValue nextRunTime() {
      return timestamp.add(handle().scheduleInterval());
   }
}
