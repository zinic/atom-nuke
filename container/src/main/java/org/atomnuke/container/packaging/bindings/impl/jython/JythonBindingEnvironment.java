package org.atomnuke.container.packaging.bindings.impl.jython;

import java.io.InputStream;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.bindings.LanguageDescriptor;
import org.atomnuke.container.packaging.bindings.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.plugin.Environment;
import org.python.core.Options;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 *
 * @author zinic
 */
public class JythonBindingEnvironment implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.PYTHON, ".py");

   private final PythonInterpreter pythonInterpreter;
   private final JythonEnvironment jythonEnvironment;

   public JythonBindingEnvironment() {
      this(false);
   }

   public JythonBindingEnvironment(boolean beVerbose) {
      if (!beVerbose) {
         // Quiet Jython
         Options.verbose = -1;
      }

      final ClassLoader cl = Thread.currentThread().getContextClassLoader();

      final PySystemState systemState = new PySystemState();
      systemState.setClassLoader(cl);

      pythonInterpreter = new PythonInterpreter(null, systemState);

//      pythonInterpreter.setErr(outputStream);
//      pythonInterpreter.setOut(outputStream);

      jythonEnvironment = new JythonEnvironment(pythonInterpreter, cl);
   }

   @Override
   public Environment environment() {
      return jythonEnvironment;
   }

   @Override
   public void load(Resource resource) throws PackageLoadingException {
      try {
         final InputStream in = resource.location().toURL().openStream();
         pythonInterpreter.execfile(in);

         in.close();
      } catch (Exception ex) {
         throw new PackageLoadingException("Failed to load python script. Reason: " + ex.getMessage(), ex);
      }
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }
}
