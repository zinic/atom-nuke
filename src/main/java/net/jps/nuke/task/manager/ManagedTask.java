package net.jps.nuke.task.manager;

import java.util.UUID;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface ManagedTask extends Runnable {

   void destroy(TaskContext taskContext);

   boolean isReentrant();
   
   UUID id();
   
   boolean canceled();

   void cancel();
   
   TimeValue nextPollTime();
}
