package net.jps.nuke.task;

import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.task.lifecycle.InitializationException;
import net.jps.nuke.util.TimeValue;

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
    * Returns the polling interval of this task.
    *
    * @return
    */
   TimeValue interval();

   /**
    * Adds an AtomListener to this task. The listener will begin receiving ATOM
    * events during the task's next execution.
    *
    * @param listener
    * @return
    */
   void addListener(AtomListener listener) throws InitializationException;
}
