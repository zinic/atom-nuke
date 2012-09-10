package org.atomnuke.bindings.classpath;

import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.bindings.loader.Loader;
import org.atomnuke.bindings.loader.NopLoader;
import org.atomnuke.config.model.LanguageType;

/**
 *
 * @author zinic
 */
public class ClasspathBindingContext implements BindingContext {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVA);

   public ClasspathBindingContext() {
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public Loader loader() {
      return NopLoader.instance();
   }

   @Override
   public boolean hasRef(String ref) {
      try {
         Class.forName(ref);
         return true;
      } catch (ClassNotFoundException cnfe) {
         return false;
      }
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String className) throws BindingInstantiationException {
      try {
         final Class concreteClass = Class.forName(className);

         return interfaceType.cast(concreteClass.newInstance());
      } catch (Exception ex) {
         throw new BindingInstantiationException("Failed to instantiate, " + className + ". Reason: " + ex.getMessage(), ex);
      }
   }
}
