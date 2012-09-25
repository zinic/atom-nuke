package org.atomnuke.plugin;

/**
 *
 * @author zinic
 */
public abstract class AbstractInstanceEnvironment<T> implements InstanceEnvironment<T> {

   private final T instance;

   public AbstractInstanceEnvironment(T instance) {
      this.instance = instance;
   }

   @Override
   public final T getInstance() {
      return instance;
   }
}
