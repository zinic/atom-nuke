package org.atomnuke.task.context;

import java.util.Map;
import org.atomnuke.task.Tasker;

/**
 *
 * @author zinic
 */
public interface TaskContext {

   /**
    *
    * @return
    */
   Tasker submitter();

   /**
    * 
    * @return
    */
   Map<String, String> instanceParameters();
}
