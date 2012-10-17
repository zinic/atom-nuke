package org.atomnuke.task;

import java.util.UUID;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface TaskHandle {

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
