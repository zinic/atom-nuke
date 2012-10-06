package org.atomnuke.bindings.java;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.bindings.context.ClassLoaderEnvironment;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class ClassLoaderBindingContext implements BindingContext {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVA, ".class");

   private static class NukeSimpleClassLoader extends ClassLoader {

      public void defineClass(String name, byte[] bytes) {
         super.defineClass(name, bytes, 0, bytes.length);
      }
   }

   private final ClassLoaderEnvironment environment;
   private final NukeSimpleClassLoader classLoader;

   public ClassLoaderBindingContext() {
      classLoader = new NukeSimpleClassLoader();
      environment = new ClassLoaderEnvironment(classLoader);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public void load(URI in) throws BindingLoaderException {
      final String classPath = in.getPath().replace(".class", "").replace("/", ".");

      try {
         final InputStream inputStream = in.toURL().openStream();

         classLoader.defineClass(classPath, RawInputStreamReader.instance().readFully(inputStream));
      } catch (IOException ioe) {
         throw new BindingLoaderException(ioe);
      }
   }

   @Override
   public boolean hasRef(String ref) {
      try {
         classLoader.loadClass(ref);
         return true;
      } catch (ClassNotFoundException classNotFoundException) {
         return false;
      }
   }

   @Override
   public Environment environment() {
      return environment;
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException {
      try {
         final Class hrefClass = classLoader.loadClass(href);
         final Object instance = hrefClass.newInstance();

         return interfaceType.cast(instance);
      } catch (Exception ex) {
         throw new BindingInstantiationException(ex);
      }
   }
}
