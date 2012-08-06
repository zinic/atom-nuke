package org.atomnuke.bindings.jython;

import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.LanguageDescriptor;
import org.atomnuke.bindings.LanguageDescriptorImpl;
import org.atomnuke.bindings.Loader;
import org.atomnuke.config.model.LanguageType;
import org.python.core.Options;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class PythonInterpreterContext implements BindingContext {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.PYTHON, ".py");
   private final PythonInterpreter pythonInterpreter;
   private final JythonLoader loader;

   public PythonInterpreterContext() {
      this(false);
   }

   public PythonInterpreterContext(boolean beVerbose) {
      if (!beVerbose) {
         // Quiet Jython
         Options.verbose = -1;
      }

//      pythonInterpreter.setErr(outputStream);
//      pythonInterpreter.setOut(outputStream);

      pythonInterpreter = new PythonInterpreter();
      loader = new JythonLoader(pythonInterpreter);
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException {
      final PyObject pyClass = pythonInterpreter.get(href);

      // Create a new object reference of the Jython class store into PyObject
      final PyObject newObj = pyClass.__call__();

      // Call __tojava__ method on the new object along with the interface name
      // to create the java bytecode
      return (T) newObj.__tojava__(interfaceType);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public Loader loader() {
      return loader;
   }
}
