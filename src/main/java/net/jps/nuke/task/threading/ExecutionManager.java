package net.jps.nuke.task.threading;

import java.util.UUID;

/**
 *
 * @author zinic
 */
public interface ExecutionManager {

   void queue(Runnable r);

   void submit(UUID id, Runnable r);

   boolean submitted(UUID id);

   boolean draining();

   void destroy();
}
