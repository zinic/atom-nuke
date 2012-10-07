package org.atomnuke.container.packaging;

import org.atomnuke.bindings.PackageBindings;

/**
 *
 * @author zinic
 */
public class PackageContextImpl implements PackageContext {

   private final PackageBindings bindingContextManager;
   private final String name;

   public PackageContextImpl(String name, PackageBindings bindingsManager) {
      this.bindingContextManager = bindingsManager;
      this.name = name;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public PackageBindings packageBindings() {
      return bindingContextManager;
   }
}
