package org.atomnuke;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
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
    * Starts the Nuke instance.
    */
   void start();

   /**
    * Stops the nuke instance.
    */
   void destroy();

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
   Task follow(AtomSource source, TimeValue pollingInterval) throws InitializationException;

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
   Task follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) throws InitializationException;
}
