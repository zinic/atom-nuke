package net.jps.nuke.task.lifecycle;

import net.jps.nuke.task.context.TaskContext;

/**
 *
 * @author zinic
 */
public interface TaskLifeCycle {

   void init(TaskContext tc) throws InitializationException;

   void destroy(TaskContext tc) throws DestructionException;
}
