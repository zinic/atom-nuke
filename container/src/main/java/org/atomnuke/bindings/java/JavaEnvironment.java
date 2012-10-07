package org.atomnuke.bindings.java;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.env.ClassLoaderEnvironment;
import org.atomnuke.bindings.scanner.ClassLoaderScanner;
import org.atomnuke.bindings.scanner.ClassVisitor;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceDescriptor;
import org.atomnuke.service.ServiceLifeCycle;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.context.ServiceContextImpl;
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
   public List<Service> instantiateServices() throws ReferenceInstantiationException {
      final ServiceContext serviceContext = new ServiceContextImpl(Collections.EMPTY_MAP);
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
               final ServiceLifeCycle serviceInstance = (ServiceLifeCycle) serviceClass.newInstance();
               serviceInstance.init(serviceContext);
               
               builtServices.add(new ServiceDescriptor(null, new InstanceContextImpl<ServiceLifeCycle>(this, serviceInstance)));
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
