package org.atomnuke.bindings.rhinojs;

import org.atomnuke.bindings.context.ClassLoaderContext;

/**
 *
 * @author zinic
 */
public class RhinoInstanceContext<T> extends ClassLoaderContext<T> {

   public RhinoInstanceContext(ClassLoader classLoader, T instance) {
      super(classLoader, instance);
   }
}
