package org.atomnuke;

import org.atomnuke.kernel.shutdown.ShutdownHook;
import org.atomnuke.service.introspection.ServicesInterrogator;
import org.atomnuke.task.manager.AtomTasker;

/**
 * The Nuke interface.
 *
 * This interface allows for thread-safe interaction with the Nuke scheduler.
 *
 * @author zinic
 */
public interface Nuke {

   NukeEnvironment nukeEnvironment();

   AtomTasker atomTasker();

   ShutdownHook shutdownHook();

   void start();

   void destroy();
}
