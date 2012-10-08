package org.atomnuke.container.packaging.bindings.impl.rhinojs;

import java.util.Collections;
import java.util.List;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import org.atomnuke.container.packaging.bindings.environment.ClassLoaderEnvironment;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public class RhinoJsEnvrionment extends ClassLoaderEnvironment {

   private final ScriptEngine jsEngine;

   public RhinoJsEnvrionment(ScriptEngine jsEngine, ClassLoader classLoader) {
      super(classLoader);

      this.jsEngine = jsEngine;
   }

   @Override
   public List<Service> services() {
      return Collections.EMPTY_LIST;
   }

   @Override
   public boolean hasReference(String referenceName) {
      return jsEngine.get(referenceName) != null;
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String href) throws ReferenceInstantiationException {
      try {
         final Invocable inv = (Invocable) jsEngine;
         final Object builderFunctionRef = jsEngine.get(href);

         if (builderFunctionRef != null) {
            final Object instanceRef = inv.invokeFunction(href, new Object[0]);

            return inv.getInterface(instanceRef, interfaceType);
         }

         throw new ReferenceInstantiationException("Unable to lookup function: " + href + " in the javascript engine global scope.");
      } catch (Exception ex) {
         throw new ReferenceInstantiationException(ex.getMessage(), ex);
      }
   }
}
