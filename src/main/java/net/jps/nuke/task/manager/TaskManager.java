package net.jps.nuke.task.manager;

import java.util.List;
import net.jps.nuke.task.Tasker;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface TaskManager extends Tasker {

   void destroy();

   List<ManagedTask> tasks();

   TimeValue scheduleTasks();
}
