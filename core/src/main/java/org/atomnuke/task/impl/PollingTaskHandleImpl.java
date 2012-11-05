package org.atomnuke.task.impl;

import org.atomnuke.task.PollingTaskHandle;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class PollingTaskHandleImpl extends TaskHandleImpl implements PollingTaskHandle {

   private final TimeValue interval;

   public PollingTaskHandleImpl(boolean reenterant, TimeValue interval, CancellationRemote cancelationRemote) {
      super(reenterant, cancelationRemote);

      this.interval = interval;
   }

   @Override
   public TimeValue scheduleInterval() {
      return interval;
   }
}
