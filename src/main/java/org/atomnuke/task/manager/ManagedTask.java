package org.atomnuke.task.manager;

import java.util.UUID;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.util.TimeValue;

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
   
   void scheduled();
}
