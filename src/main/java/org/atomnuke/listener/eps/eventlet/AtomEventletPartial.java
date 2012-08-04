package org.atomnuke.listener.eps.eventlet;

import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

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
