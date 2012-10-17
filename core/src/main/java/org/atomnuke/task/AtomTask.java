package org.atomnuke.task;

import org.atomnuke.listener.AtomListener;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 * A Nuke Task represents an ATOM polling task. The task executes at a regular
 * interval defined by the interval method. The task, when executed, will
 * dispatch ATOM events to any listeners registered to it.
 *
 * @author zinic
 */
public interface AtomTask extends TaskHandle {

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
   CancellationRemote addListener(AtomListener listener);

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
   CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext);
}
