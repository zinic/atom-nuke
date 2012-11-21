package org.atomnuke.plugin.env;

import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.ReferenceInstantiationException;

/**
 *
 * @author zinic
 */
public abstract class ClassLoaderEnvironment implements Environment {

   private final ThreadLocal<ClassLoader> previousContext;
   private final ClassLoader classLoader;

   public ClassLoaderEnvironment(ClassLoader classLoader) {
      this.classLoader = classLoader;

      previousContext = new ThreadLocal<ClassLoader>();
   }

   protected final ClassLoader classLoader() {
      return classLoader;
   }

   @Override
   public void stepInto() {
      final Thread currentThread = Thread.currentThread();

      previousContext.set(currentThread.getContextClassLoader());
      currentThread.setContextClassLoader(classLoader);
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String referenceName) throws ReferenceInstantiationException {
      try {
         final Class hrefClass = classLoader.loadClass(referenceName);
         final Object instance = hrefClass.newInstance();

         return interfaceType.cast(instance);
      } catch (Exception ex) {
         throw new ReferenceInstantiationException(ex);
      }
   }

   @Override
   public boolean hasReference(String referenceName) {
      try {
         classLoader.loadClass(referenceName);
         return true;
      } catch (ClassNotFoundException cnfe) {
      }

      return false;
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

   @Override
   public String toString() {
      return "ClassLoaderEnvironment{" + "classLoader=" + classLoader + '}';
   }
}
