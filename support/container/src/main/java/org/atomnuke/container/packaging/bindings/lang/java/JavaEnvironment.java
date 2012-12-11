package org.atomnuke.container.packaging.bindings.lang.java;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.plugin.env.ClassLoaderEnvironment;
import org.atomnuke.container.packaging.bindings.lang.java.scanner.ClassLoaderScanner;
import org.atomnuke.container.packaging.bindings.lang.java.scanner.ClassVisitor;
import org.atomnuke.container.packaging.classloader.IdentityClassLoader;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class JavaEnvironment extends ClassLoaderEnvironment {

   private static final Logger LOG = LoggerFactory.getLogger(JavaEnvironment.class);
   private final ResourceManager resourceManager;
   private final ClassLoader envClassloader;

   public JavaEnvironment(ClassLoader parent, ResourceManager resourceManager) {
      this(resourceManager, new IdentityClassLoader(parent, resourceManager));
   }

   private JavaEnvironment(ResourceManager resourceManager, ClassLoader envClassloader) {
      super(envClassloader);

      this.envClassloader = envClassloader;
      this.resourceManager = resourceManager;
   }

   @Override
   public List<Service> services() {
      final List<Service> builtServices = new LinkedList<Service>();

      try {
         stepInto();

         for (String serviceClassName : allServiceClassNames()) {
            try {
               final Class serviceClazz = envClassloader.loadClass(serviceClassName);
               builtServices.add((Service) serviceClazz.newInstance());
            } catch (Exception ex) {
               LOG.error("Service init failed for service class: " + serviceClassName + " - Reason: " + ex.getMessage(), ex);
            }
         }
      } finally {
         stepOut();
      }

      return builtServices;
   }

   private List<String> allServiceClassNames() {
      final List<String> discoveredServiceClasses = new LinkedList<String>();
      
      final ClassLoader scannerClassLoader = new IdentityClassLoader(resourceManager);
      final ClassLoaderScanner classLoaderScanner = new ClassLoaderScanner(resourceManager, scannerClassLoader);

      classLoaderScanner.scan(new ClassVisitor() {
         @Override
         public void visit(Class<?> clazz) {
            final Annotation nukeSvcAnnotation = clazz.getAnnotation(NukeService.class);

            if (nukeSvcAnnotation != null) {
               discoveredServiceClasses.add(String.valueOf(clazz.getName()));
            }
         }
      });

      return discoveredServiceClasses;
   }
}