package org.atomnuke.container.packaging.bindings.environment;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import org.atomnuke.container.packaging.bindings.lang.java.JavaBindingEnvironment;
import org.atomnuke.container.packaging.bindings.lang.jython.JythonBindingEnvironment;
import org.atomnuke.container.packaging.bindings.lang.rhinojs.RhinoInterpreterContext;
import org.atomnuke.container.packaging.classloader.IdentityClassLoader;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public class BindingEnvironmentManagerImpl implements BindingEnvironmentFactory {

   private final ClassLoader rootClassloader;

   public BindingEnvironmentManagerImpl(ResourceManager resourceManager) {
      rootClassloader = new IdentityClassLoader(resourceManager);
   }

   @Override
   public List<BindingEnvironment> newEnviornment(ResourceManager resourceManager) {
      final List<BindingEnvironment> bindingContexts = new LinkedList<BindingEnvironment>();

      bindingContexts.add(new JavaBindingEnvironment(rootClassloader, resourceManager));
      bindingContexts.add(new JythonBindingEnvironment());
      bindingContexts.add(RhinoInterpreterContext.newInstance());

      return bindingContexts;
   }
}
