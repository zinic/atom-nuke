package org.atomnuke.context;

/**
 *
 * @author zinic
 */
public abstract class AbstractInstanceContext<T> implements InstanceContext<T> {

   private final T instance;

   public AbstractInstanceContext(T instance) {
      this.instance = instance;
   }

   @Override
   public final T getInstance() {
      return instance;
   }
}
