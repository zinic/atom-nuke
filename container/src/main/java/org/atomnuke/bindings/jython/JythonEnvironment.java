package org.atomnuke.bindings.jython;

import java.util.Collections;
import java.util.List;
import org.atomnuke.bindings.env.ClassLoaderEnvironment;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class JythonEnvironment extends ClassLoaderEnvironment {

   private final PythonInterpreter pythonInterpreter;

   public JythonEnvironment(PythonInterpreter pythonInterpreter, ClassLoader classLoader) {
      super(classLoader);

      this.pythonInterpreter = pythonInterpreter;
   }

   @Override
   public List<Service> services() {
      return Collections.EMPTY_LIST;
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String referenceName) throws ReferenceInstantiationException {
      final PyObject pyClass = pythonInterpreter.get(referenceName);

      // Create a new object reference of the Jython class store into PyObject
      final PyObject newObj = pyClass.__call__();

      // Call __tojava__ method on the new object along with the interface name
      // to create the java bytecode
      return (T) newObj.__tojava__(interfaceType);
   }

   @Override
   public boolean hasReference(String referenceName) {
      return pythonInterpreter.get(referenceName) != null;
   }
}
