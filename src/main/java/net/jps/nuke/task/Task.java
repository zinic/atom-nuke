package net.jps.nuke.task;

import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.util.remote.CancellationRemote;

/**
 * A Nuke Task represents an ATOM feed polling task. The task executes at a
 * regular interval defined by the method interval(). The task, when executed,
 * will dispatch ATOM events to any listeners registered to it.
 *
 * @author zinic
 */
public interface Task {

   /**
    * Cancels the task's next execution. This stops all of the listeners
    * assigned to this task.
    */
   void cancel();

   /**
    * Checks to see if this task has been canceled.
    *
    * @return
    */
   boolean canceled();

   /**
    * Returns the next scheduled polling time.
    *
    * @return
    */
   TimeValue nextPollTime();

   /**
    * Returns the polling interval of this task.
    *
    * @return
    */
   TimeValue interval();

   /**
    * Adds an AtomListener to this task. The listener will begin receiving ATOM
    * events during the task's next execution. This method returns a
    * CancellationRemote that may be utilized to remove the listener at any
    * time.
    *
    * @param listener
    * @return
    */
   CancellationRemote addListener(AtomListener listener);

   /**
    * Adds an AtomListener to this task. The listener will begin receiving ATOM
    * events during the task's next execution.
    *
    * @param listener
    * @param listenerCancelationRemote
    */
   void addListener(AtomListener listener, CancellationRemote listenerCancelationRemote);
}
