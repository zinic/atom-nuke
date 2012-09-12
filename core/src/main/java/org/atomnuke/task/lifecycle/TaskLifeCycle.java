package org.atomnuke.task.lifecycle;

import org.atomnuke.task.context.TaskContext;

/**
 * The task life-cycle defines two methods that will be called once during the
 * lifetime of the object bound to the life-cycle.
 *
 * The init method will always be called before the destroy method and ideally
 * will be called when the object is being prepared for use. The init method
 * will never be called after the destroy method.
 *
 * The destroy method may be called at any time and may be called before the
 * init method. Once this method is called, it is assumed that the object will
 * no longer be in use.
 *
 * @author zinic
 */
public interface TaskLifeCycle {

   /**
    * Initializes this task.
    *
    * @param tc task context
    * @throws InitializationException
    */
   void init(TaskContext tc) throws InitializationException;

   void destroy(TaskContext tc) throws DestructionException;
}
