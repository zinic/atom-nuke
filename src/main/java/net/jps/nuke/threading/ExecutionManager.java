package net.jps.nuke.threading;

import net.jps.nuke.listener.driver.RegisteredListenerDriver;
import net.jps.nuke.task.ManagedTask;

/**
 *
 * @author zinic
 */
public interface ExecutionManager {

   void submit(RegisteredListenerDriver listenerDriver);

   void submit(ManagedTask task);

   void destroy();

   boolean submitted(ManagedTask task);
}
