package org.atomnuke.task.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   TaskHandle queueTask(InstanceContext<? extends ReclaimableTask> instanceContext);

   TaskHandle pollTask(InstanceContext<? extends ReclaimableTask> instanceContext, TimeValue pollingInterval);
}
