package org.atomnuke.container.packaging;

import org.atomnuke.bindings.BindingContextManager;

/**
 *
 * @author zinic
 */
public class PackageContextImpl implements PackageContext {

   private final BindingContextManager bindingContextManager;
   private final String name;

   public PackageContextImpl(String name, BindingContextManager bindingsManager) {
      this.bindingContextManager = bindingsManager;
      this.name = name;
   }

   @Override
   public String name() {
      return name;
   }

   public BindingContextManager bindingContextManager() {
      return bindingContextManager;
   }
}
