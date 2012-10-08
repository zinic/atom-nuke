package org.atomnuke.container.packaging.bindings.impl.java.scanner;

import org.atomnuke.container.packaging.archive.ResourceType;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.packaging.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ClassLoaderScanner {

   private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderScanner.class);

   private final ResourceManager resourceManager;
   private final ClassLoader classLoader;

   public ClassLoaderScanner(ResourceManager resourceManager, ClassLoader classLoader) {
      this.resourceManager = resourceManager;
      this.classLoader = classLoader;
   }

   private void logLoadFailure(String classPath, Throwable cause) {
      LOG.debug("Linkage error detected with class: " + classPath + ". Unable to resolve dependency: " + cause.getMessage());
   }

   public void scan(ClassVisitor... classVisitors) {
      for (Resource resource : resourceManager.resources()) {
         if (resource.type() == ResourceType.CLASS) {
            final String classPath = ResourceUtil.instance().relativePathToClassPath(resource.relativePath());

            try {
               final Class loadedClass = classLoader.loadClass(classPath);

               for (ClassVisitor classVisitor : classVisitors) {
                  classVisitor.visit(loadedClass);
               }
            } catch (NoClassDefFoundError noClassDefFoundError) {
               logLoadFailure(classPath, noClassDefFoundError);
            } catch (ClassNotFoundException classNotFoundException) {
               logLoadFailure(classPath, classNotFoundException);
            }
         }
      }
   }
}
