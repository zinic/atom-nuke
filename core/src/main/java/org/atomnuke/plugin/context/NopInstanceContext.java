package org.atomnuke.plugin.context;

import org.atomnuke.plugin.env.NopInstanceEnvironment;

/**
 *
 * @author zinic
 */
public class NopInstanceContext<T> extends InstanceContextImpl<T> {

   public NopInstanceContext(T instance) {
      super(NopInstanceEnvironment.getInstance(), instance);
   }
}
