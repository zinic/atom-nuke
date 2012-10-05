package org.atomnuke.bindings.jython;

import java.io.InputStream;
import java.net.URI;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.Loader;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class JythonLoader implements Loader {

   private final PythonInterpreter pythonInterpreter;

   public JythonLoader(PythonInterpreter pythonInterpreter) {
      this.pythonInterpreter = pythonInterpreter;
   }

   @Override
   public void load(URI input) throws BindingLoaderException {
      try {
         final InputStream in = input.toURL().openStream();

         pythonInterpreter.execfile(in);

         in.close();
      } catch (Exception ex) {
         throw new BindingLoaderException("Failed to load python script. Reason: " + ex.getMessage(), ex);
      }
   }
}
