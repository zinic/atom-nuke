package org.atomnuke.bindings.jython;

import java.io.InputStream;
import java.net.URI;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.context.ClassLoaderEnvironment;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.plugin.Environment;
import org.python.core.Options;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class PythonInterpreterContext implements BindingContext {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.PYTHON, ".py");

   private final PythonInterpreter pythonInterpreter;
   private final Environment pythonEnvironment;

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
      
      final ClassLoader loader = new ClassLoader() {
      };

      pythonEnvironment = new ClassLoaderEnvironment(loader);

      final PySystemState systemState = new PySystemState();
      systemState.setClassLoader(loader);

      pythonInterpreter = new PythonInterpreter(new PyObject(), systemState);
   }

   @Override
   public Environment environment() {
      return pythonEnvironment;
   }

   @Override
   public boolean hasRef(String ref) {
      return pythonInterpreter.get(ref) != null;
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

   @Override
   public <T> T instantiate(Class<T> interfaceType, String ref) throws BindingInstantiationException {
      final PyObject pyClass = pythonInterpreter.get(ref);

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
}
