package org.atomnuke.bindings.rhinojs;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
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
   public void load(InputStream in) throws BindingLoaderException {
      try {
         javascriptEngine.eval(new InputStreamReader(in));
      } catch (ScriptException se) {
         throw new BindingLoaderException("Javascript error: " + se.getMessage(), se);
      }
   }
}
