package net.jps.nuke.listener.eps.eventlet;

import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public abstract class AtomEventletPartial implements AtomEventlet {

   @Override
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
   }
}
