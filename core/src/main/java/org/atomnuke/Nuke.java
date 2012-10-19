package org.atomnuke;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.kernel.shutdown.ShutdownHook;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.manager.AtomTasker;
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
    * Helper method for following a given source at a defined polling interval.
    * This has the same effect as calling follow on the Tasker interface.This
    * calls the follow method by wrapping the given AtomSource in a
    * SimpleInstanceContext.
    *
    * @param source the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   AtomTask follow(AtomSource source, TimeValue pollingInterval);

   /**
    * Helper method for following a given source at a defined polling interval.
    * This has the same effect as calling follow on the Tasker interface.
    *
    * @param source the instance context of the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval);

   AtomTasker tasker();

   ShutdownHook shutdownHook();

   void start();

   void destroy();
}
