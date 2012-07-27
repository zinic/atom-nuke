package net.jps.nuke.task;

import net.jps.nuke.util.remote.CancellationRemote;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.listener.AtomListener;

/**
 *
 * @author zinic
 */
public interface Task {

   /**
    * Cancels the crawler's next execution. This stops all of the listeners
    * assigned to this crawler.
    */
   void cancel();

   boolean canceled();

   TimeValue nextPollTime();

   TimeValue interval();

   CancellationRemote addListener(AtomListener listener);

   void addListener(AtomListener listener, CancellationRemote listenerCancelationRemote);
}
