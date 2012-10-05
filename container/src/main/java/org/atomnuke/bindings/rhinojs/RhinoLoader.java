package org.atomnuke.bindings.rhinojs;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import javax.script.ScriptEngine;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.Loader;

/**
 *
 * @author zinic
 */
public class RhinoLoader implements Loader {

   private final ScriptEngine javascriptEngine;

   public RhinoLoader(ScriptEngine javascriptEngine) {
      this.javascriptEngine = javascriptEngine;
   }

   @Override
   public void load(URI inputLocation) throws BindingLoaderException {
      try {
         final InputStream in = inputLocation.toURL().openStream();

         javascriptEngine.eval(new InputStreamReader(in));

         in.close();
      } catch (Exception se) {
         throw new BindingLoaderException("Failed to load Javascript file. Reason: " + se.getMessage(), se);
      }
   }
}
