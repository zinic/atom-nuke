package org.atomnuke.plugin.local;

import org.atomnuke.plugin.AbstractInstanceEnvironment;
import org.atomnuke.plugin.AbstractInstanceEnvironment;

/**
 *
 * @author zinic
 */
public class LocalInstanceEnvironment<T> extends AbstractInstanceEnvironment {

   public LocalInstanceEnvironment(Object instance) {
      super(instance);
   }

   @Override
   public void stepOut() {
   }

   @Override
   public void stepInto() {
   }
}
