package org.atomnuke.task.context;

import org.atomnuke.context.Context;
import org.atomnuke.task.Tasker;

/**
 *
 * @author zinic
 */
public interface TaskContext extends Context {

   /**
    *
    * @return
    */
   Tasker submitter();
}
