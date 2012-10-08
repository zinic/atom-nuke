package org.atomnuke.bindings.java;

import org.atomnuke.bindings.PackageLoaderException;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.atomnuke.container.packaging.classloader.IdentityClassLoader;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.packaging.resource.ResourceUtil;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class JavaBindingEnvironment implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(LanguageType.JAVA, ".class");
   private final JavaEnvironment environment;
   private final IdentityClassLoader classLoader;
   private final ResourceManager resourceManager;

   public JavaBindingEnvironment(ResourceManager resourceManager) {
      this.resourceManager = resourceManager;

      classLoader = new IdentityClassLoader(resourceManager);
      environment = new JavaEnvironment(resourceManager, classLoader);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public void load(Resource resource) throws PackageLoaderException {
      if (resource.type() == ResourceType.CLASS) {
         final String classPath = ResourceUtil.instance().relativePathToClassPath(resource.relativePath());
         resourceManager.alias(classPath, resource.location());
      }
   }

   @Override
   public Environment environment() {
      return environment;
   }
}
