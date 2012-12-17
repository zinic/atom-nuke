package org.atomnuke.task.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.polling.PollingController;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   /**
    * Runs the task once and only once as soon as possible.
    * 
    * @param instanceContext
    * @return 
    */
   void queueAction(InstanceContext<? extends ReclaimableTask> instanceContext);
   
   /**
    * Runs the task according to the polling controller so long as the task has not been canceled.
    * @param instanceContext
    * @param pollingController
    * @return 
    */
   TaskHandle queueTask(InstanceContext<? extends ReclaimableTask> instanceContext, PollingController pollingController);

   /**
    * Runs the task according to a polling interval so long as the task has not been canceled.
    * 
    * @param instanceContext
    * @param pollingInterval
    * @return 
    */
   TaskHandle pollTask(InstanceContext<? extends ReclaimableTask> instanceContext, TimeValue pollingInterval);
}
