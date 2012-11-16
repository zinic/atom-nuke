package org.atomnuke.container.packaging.bindings.lang.java;

import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.lang.LanguageDescriptor;
import org.atomnuke.container.packaging.bindings.lang.LanguageDescriptorImpl;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.packaging.resource.ResourceUtil;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class JavaBindingEnvironment implements BindingEnvironment {

   private static final LanguageDescriptor LANGUAGE_DESCRIPTOR = new LanguageDescriptorImpl(BindingLanguage.JAVA, ".class");

   private final JavaEnvironment environment;
   private final ResourceManager resourceManager;

   public JavaBindingEnvironment(ClassLoader parent, ResourceManager resourceManager) {
      this.resourceManager = resourceManager;

      environment = new JavaEnvironment(parent, resourceManager);
   }

   @Override
   public LanguageDescriptor language() {
      return LANGUAGE_DESCRIPTOR;
   }

   @Override
   public void load(Resource resource) throws PackageLoadingException {
      if (resource.type() == ResourceType.CLASS) {
         final String classPath = ResourceUtil.instance().relativePathToClassPath(resource.relativePath());
         
         // Alias the classpath only if we need to
         if (resourceManager.lookup(classPath) == null) {
            resourceManager.alias(classPath, resource.uri());
         }
      }
   }

   @Override
   public Environment environment() {
      return environment;
   }
}
