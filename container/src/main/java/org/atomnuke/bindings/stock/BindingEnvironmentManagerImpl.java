package org.atomnuke.bindings.stock;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.*;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.bindings.java.ClassLoaderBindingContext;
import org.atomnuke.bindings.jython.JythonBindingEnvironment;
import org.atomnuke.bindings.rhinojs.RhinoInterpreterContext;

/**
 *
 * @author zinic
 */
public class BindingEnvironmentManagerImpl implements BindingEnvironmentManager {

   @Override
   public List<BindingEnvironment> newEnviornmentList() {
      final List<BindingEnvironment> bindingContexts = new LinkedList<BindingEnvironment>();

      bindingContexts.add(new ClassLoaderBindingContext());
      bindingContexts.add(new JythonBindingEnvironment());
      bindingContexts.add(RhinoInterpreterContext.newInstance());

      return bindingContexts;
   }
}
