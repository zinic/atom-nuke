package org.atomnuke.bindings.context;

import org.atomnuke.context.AbstractInstanceContext;

/**
 *
 * @author zinic
 */
public class ClassLoaderContext<T> extends AbstractInstanceContext<T> {

   private final ThreadLocal<ClassLoader> previousContext;
   private final ClassLoader classLoader;

   public ClassLoaderContext(ClassLoader classLoader, T instance) {
      super(instance);

      this.classLoader = classLoader;
      previousContext = new ThreadLocal<ClassLoader>();
   }

   @Override
   public void stepInto() {
      final Thread currentThread = Thread.currentThread();

      previousContext.set(currentThread.getContextClassLoader());
      currentThread.setContextClassLoader(classLoader);
   }

   @Override
   public void stepOut() {
      if (previousContext == null) {
         throw new IllegalStateException("ClassLoader was not stepped into.");
      }

      final Thread currentThread = Thread.currentThread();
      currentThread.setContextClassLoader(previousContext.get());

      previousContext.remove();
   }
}
