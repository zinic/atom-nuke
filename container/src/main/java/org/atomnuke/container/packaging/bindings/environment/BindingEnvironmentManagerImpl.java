package org.atomnuke.container.packaging.bindings.environment;

import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.impl.java.JavaBindingEnvironment;
import org.atomnuke.container.packaging.bindings.impl.jython.JythonBindingEnvironment;
import org.atomnuke.container.packaging.bindings.impl.rhinojs.RhinoInterpreterContext;
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
