package org.atomnuke.container.packaging.bindings.impl.rhinojs;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.PackageLoaderException;
import org.atomnuke.container.packaging.bindings.LanguageDescriptor;
import org.atomnuke.container.packaging.bindings.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class RhinoInterpreterContext implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVASCRIPT, ".js");

   public static RhinoInterpreterContext newInstance() {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final ScriptEngineManager scriptEngineManager = new ScriptEngineManager(classLoader);

      return new RhinoInterpreterContext(scriptEngineManager, classLoader);
   }

   private final RhinoJsEnvrionment rhinoJsEnvrionment;
   private final ScriptEngine jsEngine;

   public RhinoInterpreterContext(ScriptEngineManager scriptEngineManager, ClassLoader internalLoader) {
      jsEngine = scriptEngineManager.getEngineByName("javascript");
      rhinoJsEnvrionment = new RhinoJsEnvrionment(jsEngine, internalLoader);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public Environment environment() {
      return rhinoJsEnvrionment;
   }

   @Override
   public void load(Resource resource) throws PackageLoaderException {
      try {
         final InputStream in = resource.location().toURL().openStream();
         jsEngine.eval(new InputStreamReader(in));

         in.close();
      } catch (Exception se) {
         throw new PackageLoaderException("Failed to load Javascript file. Reason: " + se.getMessage(), se);
      }
   }
}
