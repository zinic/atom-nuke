package org.atomnuke.container.packaging.bindings.impl.java;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.container.packaging.bindings.environment.ClassLoaderEnvironment;
import org.atomnuke.container.packaging.bindings.impl.java.scanner.ClassLoaderScanner;
import org.atomnuke.container.packaging.bindings.impl.java.scanner.ClassVisitor;
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

   private final ClassLoaderScanner classLoaderScanner;

   public JavaEnvironment(ResourceManager resourceManager, ClassLoader classLoader) {
      super(classLoader);

      classLoaderScanner = new ClassLoaderScanner(resourceManager, classLoader);
   }

   @Override
   public List<Service> services() {
      final List<Class> discoveredServiceClasses = new LinkedList<Class>();

      LOG.info("Scanning classpath...");

      classLoaderScanner.scan(new ClassVisitor() {
         @Override
         public void visit(Class<?> clazz) {
            final Annotation nukeSvcAnnotation = clazz.getAnnotation(NukeService.class);

            if (nukeSvcAnnotation != null) {
               discoveredServiceClasses.add(clazz);
            }
         }
      });

      final List<Service> builtServices = new LinkedList<Service>();

      try {
         stepInto();

         for (Class serviceClass : discoveredServiceClasses) {
            try {
               builtServices.add((Service) serviceClass.newInstance());
            } catch (Exception ex) {
               LOG.error("Service init failed for service class: " + serviceClass.getName() + " - Reason: " + ex.getMessage(), ex);
            }
         }
      } finally {
         stepOut();
      }

      return builtServices;
   }
}
