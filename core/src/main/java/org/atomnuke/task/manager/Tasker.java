package org.atomnuke.task.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.runnable.ReclaimableRunnable;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   TaskHandle queueTask(ReclaimableRunnable runnable);

   TaskHandle queueTask(InstanceContext<? extends ReclaimableRunnable> instanceContext);

   TaskHandle pollTask(ReclaimableRunnable runnable, TimeValue pollingInterval);

   TaskHandle pollTask(InstanceContext<? extends ReclaimableRunnable> instanceContext, TimeValue pollingInterval);
}
