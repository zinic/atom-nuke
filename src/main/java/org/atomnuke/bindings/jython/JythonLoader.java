package org.atomnuke.bindings.jython;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.Loader;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class JythonLoader implements Loader {

   private final PythonInterpreter pythonInterpreter;
   private final List<String> loadedHrefs;

   public JythonLoader(PythonInterpreter pythonInterpreter) {
      this.pythonInterpreter = pythonInterpreter;
      
      loadedHrefs = new LinkedList<String>();
   }

   @Override
   public void load(String href, InputStream in) throws BindingLoaderException {
      if (!loadedHrefs.contains(href)) {
         final File file = new File(href);

         try {
            final InputStream fin = new FileInputStream(file);
            pythonInterpreter.execfile(fin);

            fin.close();

            loadedHrefs.add(href);
         } catch (IOException ioe) {
            throw new BindingLoaderException("Failed to load python script: " + href + ". Reason: " + ioe.getMessage(), ioe);
         }
      }
   }
}
