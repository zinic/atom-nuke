package net.jps.nuke.task.submission;

import java.util.List;
import net.jps.nuke.task.ManagedTask;

/**
 *
 * @author zinic
 */
public interface TaskManager extends Tasker {

   void destroy();
   
   List<ManagedTask> tasks();
}
