package org.atomnuke.task;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 * A Nuke Task represents an ATOM polling task. The task executes at a regular
 * interval defined by the interval method. The task, when executed, will
 * dispatch ATOM events to any listeners registered to it.
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
    * @return true if the task has been canceled, false otherwise.
    */
   boolean canceled();

   /**
    * Returns the polling interval of this task.
    *
    * @return the time interval representing the desired polling interval of
    * this task.
    */
   TimeValue interval();

   /**
    * Adds an AtomListener to this task. This method wraps the listener and
    * passes it to the other addListener method as a SimpleInstanceContext.
    *
    * @param listener
    * @return the cancellation remote for the newly registered listener.
    */
   CancellationRemote addListener(AtomListener listener) throws InitializationException;

   /**
    * Adds an AtomListener to this task. The tasker requires that an
    * InstanceContext be give for each AtomSource to allow for the abstraction
    * of system internals like custom class loaders.
    *
    * The listener will begin receiving ATOM events during the task's next
    * execution.
    *
    * @param listener
    * @return the cancellation remote for the newly registered listener.
    */
   CancellationRemote addListener(InstanceContext<? extends AtomListener> listener) throws InitializationException;
}
