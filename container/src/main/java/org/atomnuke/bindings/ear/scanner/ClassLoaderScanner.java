package org.atomnuke.bindings.ear.scanner;

import java.lang.annotation.Annotation;
import org.atomnuke.container.service.annotation.NukeService;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author zinic
 */
public class ClassLoaderScanner {

   private static final Annotation NUKE_SVC_ANNOTATION = new NukeService() {
      @Override
      public Class<? extends Annotation> annotationType() {
         return NukeService.class;
      }
   };
   private final ClassLoader classLoader;

   public ClassLoaderScanner(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public void scan() {
      final Thread currentThread = Thread.currentThread();
      final ClassLoader threadClassLoader = currentThread.getContextClassLoader();

      try {
         currentThread.setContextClassLoader(classLoader);

         final Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(ClasspathHelper.forClassLoader(classLoader)).setScanners(new TypeAnnotationsScanner()));
         
         for (Class service : reflections.getTypesAnnotatedWith(NUKE_SVC_ANNOTATION)) {
            System.out.println(service.getName());
         }
      } finally {
         currentThread.setContextClassLoader(threadClassLoader);
      }
   }
}
