package org.atomnuke.bindings.jython;

import java.io.*;
import org.atomnuke.bindings.InterpreterContext;
import org.atomnuke.bindings.LoadResult;
import org.python.core.Options;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class PythonInterpreterContext implements InterpreterContext {

   private final PythonInterpreter pythonInterpreter;

   public PythonInterpreterContext() {
      this(false);
   }

   public PythonInterpreterContext(boolean beVerbose) {
      if (!beVerbose) {
         // Quiet Jython
         Options.verbose = -1;
      }

      pythonInterpreter = new PythonInterpreter();
   }

   @Override
   public void setStdErr(OutputStream outputStream) {
      pythonInterpreter.setErr(outputStream);
   }

   @Override
   public void setStdOut(OutputStream outputStream) {
      pythonInterpreter.setOut(outputStream);
   }

   @Override
   public LoadResult load(File file) {
      try {
         final InputStream fin = new FileInputStream(file);
         pythonInterpreter.execfile(fin);

         fin.close();
      } catch (Throwable t) {
         return new LoadResult(t);
      }

      return new LoadResult();
   }

   @Override
   public <T> JythonObjectFactory<T> newObjectFactory(Class<T> objectClass) {
      return new JythonObjectFactory<T>(pythonInterpreter, objectClass);
   }
}
