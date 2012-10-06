package org.atomnuke.bindings.context;

import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class ClassLoaderEnvironment implements Environment {

   private final ThreadLocal<ClassLoader> previousContext;
   private final ClassLoader classLoader;

   public ClassLoaderEnvironment(ClassLoader classLoader) {
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
      if (previousContext.get() == null) {
         return;
      }

      final Thread currentThread = Thread.currentThread();
      currentThread.setContextClassLoader(previousContext.get());

      previousContext.remove();
   }
}
