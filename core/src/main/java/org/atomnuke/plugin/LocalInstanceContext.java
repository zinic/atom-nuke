package org.atomnuke.plugin;

import org.atomnuke.plugin.env.LocalInstanceEnvironment;

/**
 *
 * @author zinic
 */
public class LocalInstanceContext<T> extends InstanceContextImpl<T> {

   public LocalInstanceContext(T instance) {
      super(LocalInstanceEnvironment.getInstance(), instance);
   }
}
