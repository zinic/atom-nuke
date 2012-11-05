package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.task.PollingTaskHandle;

/**
 *
 * @author zinic
 */
public class PollingTask extends AbstractPollingTask {

   public PollingTask(InstanceContext<? extends Runnable> runnable, PollingTaskHandle taskHandle) {
      super((InstanceContext<Runnable>) runnable, taskHandle);
   }
}
