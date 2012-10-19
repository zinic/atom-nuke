package org.atomnuke.task;

import java.util.UUID;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 * A Nuke TaskHandle represents a registered polling task. The task executes at
 * a regular interval defined by the interval method. The task, when executed,
 * will be dispatch to an execution queue and run.
 *
 * @author zinic
 */
public interface TaskHandle {

   /**
    * Returns the polling interval of this task.
    *
    * @return the time interval representing the desired polling interval of
    * this task.
    */
   TimeValue scheduleInterval();

   /**
    * All tasks registered with the Nuke scheduler are required to have a unique
    * ID in the form of a UUID.
    *
    * @return the tasks ID UUID.
    */
   UUID id();

   /**
    * Returns the cancellation remote for this task. The cancellation remote may
    * be utilized at any time to request that the execution of this task be
    * halted.
    *
    * @return
    */
   CancellationRemote cancellationRemote();
}
