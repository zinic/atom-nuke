package org.atomnuke.container.packaging.bindings.lang.rhinojs;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.bindings.lang.LanguageDescriptor;
import org.atomnuke.container.packaging.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class RhinoInterpreterContext implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(BindingLanguage.JAVASCRIPT, ".js");

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
   public void load(Resource resource) throws PackageLoadingException {
      try {
         final InputStream in = resource.uri().toURL().openStream();
         jsEngine.eval(new InputStreamReader(in));

         in.close();
      } catch (Exception se) {
         throw new PackageLoadingException("Failed to load Javascript file. Reason: " + se.getMessage(), se);
      }
   }
}
