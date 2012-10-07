package org.atomnuke.bindings.stock;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.*;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.bindings.java.JavaBindingEnvironment;
import org.atomnuke.bindings.jython.JythonBindingEnvironment;
import org.atomnuke.bindings.rhinojs.RhinoInterpreterContext;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public class BindingEnvironmentManagerImpl implements BindingEnvironmentFactory {

   @Override
   public List<BindingEnvironment> newEnviornmentList(ResourceManager resourceManager) {
      final List<BindingEnvironment> bindingContexts = new LinkedList<BindingEnvironment>();

      bindingContexts.add(new JavaBindingEnvironment(resourceManager));
      bindingContexts.add(new JythonBindingEnvironment());
      bindingContexts.add(RhinoInterpreterContext.newInstance());

      return bindingContexts;
   }
}
