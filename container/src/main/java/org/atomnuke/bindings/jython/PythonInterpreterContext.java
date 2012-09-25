package org.atomnuke.bindings.jython;

import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.bindings.loader.Loader;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
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
   public boolean hasRef(String ref) {
      return pythonInterpreter.get(ref) != null;
   }

   @Override
   public <T> InstanceEnvironment<T> instantiate(Class<T> interfaceType, String ref) throws BindingInstantiationException {
      final PyObject pyClass = pythonInterpreter.get(ref);

      // Create a new object reference of the Jython class store into PyObject
      final PyObject newObj = pyClass.__call__();

      // Call __tojava__ method on the new object along with the interface name
      // to create the java bytecode
      return new LocalInstanceEnvironment<T>((T) newObj.__tojava__(interfaceType));
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
