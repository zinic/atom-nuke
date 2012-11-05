package org.atomnuke.task;

import org.atomnuke.util.remote.CancellationRemote;

/**
 * A Nuke TaskHandle represents a registered polling task. The task executes at
 * a regular interval defined by the interval method. The task, when executed,
 * will be dispatch to an execution queue and run.
 *
 * @author zinic
 */
public interface TaskHandle {

   boolean reenterant();

   long id();

   /**
    * Returns the cancellation remote for this task. The cancellation remote may
    * be utilized at any time to request that the execution of this task be
    * halted.
    *
    * @return
    */
   CancellationRemote cancellationRemote();
}
