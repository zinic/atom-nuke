package org.atomnuke.bindings.rhinojs;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.bindings.loader.Loader;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.context.InstanceContext;

/**
 *
 * @author zinic
 */
public class RhinoInterpreterContext implements BindingContext {

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
   private final ScriptEngineManager scriptEngineManager;
   private final ClassLoader internalLoader;
   private final ScriptEngine jsEngine;
   private final RhinoLoader loader;

   public RhinoInterpreterContext(ScriptEngineManager scriptEngineManager, ClassLoader internalLoader) {
      this.scriptEngineManager = scriptEngineManager;
      this.internalLoader = internalLoader;

      jsEngine = scriptEngineManager.getEngineByName("javascript");
      loader = new RhinoLoader(jsEngine);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public Loader loader() {
      return loader;
   }

   @Override
   public boolean hasRef(String ref) {
      return jsEngine.get(ref) != null;
   }

   @Override
   public <T> InstanceContext<T> instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException {
      try {
         final Invocable inv = (Invocable) jsEngine;
         final Object builderFunctionRef = jsEngine.get(href);

         if (builderFunctionRef != null) {
            final Object instanceRef = inv.invokeFunction(href, new Object[0]);
            return new RhinoInstanceContext<T>(internalLoader, inv.getInterface(instanceRef, interfaceType));
         }

         throw new BindingInstantiationException("Unable to lookup function: " + href + " in the javascript engine global scope.");
      } catch (Exception ex) {
         throw new BindingInstantiationException(ex.getMessage(), ex);
      }
   }
}
