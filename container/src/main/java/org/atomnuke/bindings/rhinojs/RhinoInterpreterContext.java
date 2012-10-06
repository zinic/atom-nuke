package org.atomnuke.bindings.rhinojs;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class RhinoInterpreterContext implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVASCRIPT, ".js");

   private static class RhinoClassLoader extends ClassLoader {

      public RhinoClassLoader() {
         super(Thread.currentThread().getContextClassLoader());
      }
   }

   public static RhinoInterpreterContext newInstance() {
      final ClassLoader classLoader = new RhinoClassLoader();
      final ScriptEngineManager scriptEngineManager = new ScriptEngineManager(classLoader);

      return new RhinoInterpreterContext(scriptEngineManager, classLoader);
   }

   private final RhinoJsEnvrionment rhinoJsEnvrionment;
   private final ScriptEngine jsEngine;

   public RhinoInterpreterContext(ScriptEngineManager scriptEngineManager, ClassLoader internalLoader) {
      jsEngine = scriptEngineManager.getEngineByName("javascript");
      rhinoJsEnvrionment = new RhinoJsEnvrionment(internalLoader);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public boolean hasRef(String ref) {
      return jsEngine.get(ref) != null;
   }

   @Override
   public Environment environment() {
      return rhinoJsEnvrionment;
   }

   @Override
   public void load(String relativePath, URI inputLocation) throws BindingLoaderException {
      try {
         final InputStream in = inputLocation.toURL().openStream();
         jsEngine.eval(new InputStreamReader(in));

         in.close();
      } catch (Exception se) {
         throw new BindingLoaderException("Failed to load Javascript file. Reason: " + se.getMessage(), se);
      }
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException {
      try {
         final Invocable inv = (Invocable) jsEngine;
         final Object builderFunctionRef = jsEngine.get(href);

         if (builderFunctionRef != null) {
            final Object instanceRef = inv.invokeFunction(href, new Object[0]);

            return inv.getInterface(instanceRef, interfaceType);
         }

         throw new BindingInstantiationException("Unable to lookup function: " + href + " in the javascript engine global scope.");
      } catch (Exception ex) {
         throw new BindingInstantiationException(ex.getMessage(), ex);
      }
   }
}
