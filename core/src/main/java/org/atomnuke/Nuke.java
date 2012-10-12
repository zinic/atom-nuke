package org.atomnuke;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.kernel.shutdown.ShutdownHook;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.Tasker;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;

/**
 * The Nuke kernel interface.
 *
 * This interface allows for thread-safe interaction with the Nuke scheduler.
 *
 * @author zinic
 */
public interface Nuke {

   /**
    * Follows a given source at a defined polling interval. This calls the
    * follow method by wrapping the given AtomSource in a SimpleInstanceContext.
    *
    * @param source the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   @Deprecated
   Task follow(AtomSource source, TimeValue pollingInterval);

   /**
    * Follows a given source at a defined polling interval.
    *
    * @param source the instance context of the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   @Deprecated
   Task follow(InstanceContext<AtomSource> source, TimeValue pollingInterval);

   Tasker tasker();

   ShutdownHook shutdownHook();

   void start();

   void destroy();
}
