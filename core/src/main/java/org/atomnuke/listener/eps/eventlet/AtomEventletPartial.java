package org.atomnuke.listener.eps.eventlet;

import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 * This is an abstract class that implements empty init and destroy methods. For
 * writing simple entry event handlers without the need to initialize or destroy
 * resources, extending this class may be more desirable.
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
