package org.atomnuke.task;

import org.atomnuke.sink.AtomSink;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 * A Nuke Task represents an ATOM polling task. The task executes at a regular
 * interval defined by the interval method. The task, when executed, will
 * dispatch ATOM events to any sinks registered to it.
 *
 * @author zinic
 */
public interface AtomTask {

   /**
    * Returns the task handle.
    *
    * @return the handle of the task that this atom task represents.
    */
   TaskHandle handle();

   /**
    * Adds an AtomSink to this task. This method wraps the sink and passes
    * it to the other addSink method as a SimpleInstanceContext.
    *
    * @param sink
    * @return the cancellation remote for the newly registered sink.
    */
   CancellationRemote addSink(AtomSink sink);

   /**
    * Adds an AtomSink to this task. The tasker requires that an InstanceContext
    * be give for each AtomSource to allow for the abstraction of system
    * internals like custom class loaders.
    *
    * The sink will begin receiving ATOM events during the task's next
    * execution.
    *
    * @param sink
    * @return the cancellation remote for the newly registered sink.
    */
   CancellationRemote addSink(InstanceContext<? extends AtomSink> atomSinkContext);
}
