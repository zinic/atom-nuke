package org.atomnuke.context;

import org.atomnuke.context.AbstractInstanceContext;

/**
 *
 * @author zinic
 */
public class SimpleInstanceContext<T> extends AbstractInstanceContext {

   public SimpleInstanceContext(Object instance) {
      super(instance);
   }

   @Override
   public void stepOut() {
   }

   @Override
   public void stepInto() {
   }
}
