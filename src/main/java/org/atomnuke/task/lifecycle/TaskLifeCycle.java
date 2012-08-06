package org.atomnuke.task.lifecycle;

import org.atomnuke.task.context.TaskContext;

/**
 *
 * @author zinic
 */
public interface TaskLifeCycle {

   void init(TaskContext tc) throws InitializationException;

   void destroy(TaskContext tc) throws DestructionException;
}
