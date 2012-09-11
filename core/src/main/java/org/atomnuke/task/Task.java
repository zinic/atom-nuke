package org.atomnuke.task;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;

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

   /**
    * Adds an AtomListener to this task. The listener will begin receiving ATOM
    * events during the task's next execution.
    *
    * @param listener
    * @return
    */
   void addListenerContext(InstanceContext<? extends AtomListener> listener) throws InitializationException;
}
