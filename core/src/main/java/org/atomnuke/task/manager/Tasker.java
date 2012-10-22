package org.atomnuke.task.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.task.ReclaimableRunnable;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   TaskHandle task(ReclaimableRunnable runnable, TimeValue pollingInterval);

   TaskHandle task(InstanceContext<? extends ReclaimableRunnable> instanceContext, TimeValue pollingInterval);
}
