package org.atomnuke.bindings.classpath;

import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.LanguageDescriptor;
import org.atomnuke.bindings.LanguageDescriptorImpl;
import org.atomnuke.bindings.Loader;
import org.atomnuke.bindings.NopLoader;
import org.atomnuke.config.model.LanguageType;

/**
 *
 * @author zinic
 */
public class ClasspathObjectFactory implements BindingContext {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVA);

   public ClasspathObjectFactory() {
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
   public <T> T instantiate(Class<T> interfaceType, String className) throws BindingInstantiationException {
      try {
         final Class concreteClass = Class.forName(className);

         return interfaceType.cast(concreteClass.newInstance());
      } catch (Exception ex) {
         throw new BindingInstantiationException("Failed to instantiate, " + className + ". Reason: " + ex.getMessage(), ex);
      }
   }
}
