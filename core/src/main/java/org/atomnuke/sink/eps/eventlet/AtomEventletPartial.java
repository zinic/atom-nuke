package org.atomnuke.sink.eps.eventlet;

import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;

/**
 * This is an abstract class that implements empty init and destroy methods. For
 * writing simple entry event handlers without the need to initialize or destroy
 * resources, extending this class may be more desirable.
 *
 * @author zinic
 */
public abstract class AtomEventletPartial implements AtomEventlet {

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
