package org.atomnuke.plugin;

import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public class InstanceContextImpl<T> implements InstanceContext<T> {

   private final Environment environment;
   private final T instance;

   public InstanceContextImpl(Environment environment, T instance) {
      this.environment = environment;
      this.instance = instance;
   }

   @Override
   public T instance() {
      return instance;
   }

   @Override
   public Environment environment() {
      return environment;
   }
}
