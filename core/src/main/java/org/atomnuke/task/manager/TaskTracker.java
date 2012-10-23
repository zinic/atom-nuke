package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.ManagedTask;

/**
 *
 * @author zinic
 */
public interface TaskTracker {

   List<ManagedTask> activeTasks();

   void add(ManagedTask task);

   /**
    * A TaskTracker is considered active it can accept tasks.
    *
    * @return
    */
   boolean active();
}
