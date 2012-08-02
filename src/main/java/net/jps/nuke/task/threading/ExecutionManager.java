package net.jps.nuke.task.threading;

import java.util.UUID;

/**
 *
 * @author zinic
 */
public interface ExecutionManager {

   UUID submit(Runnable r);

   void submit(UUID id, Runnable r);

   boolean submitted(UUID id);

   void destroy();
}
