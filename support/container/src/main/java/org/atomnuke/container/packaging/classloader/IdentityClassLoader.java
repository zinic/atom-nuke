package org.atomnuke.container.packaging.classloader;

import org.atomnuke.util.collections.EmptyEnumeration;
import org.atomnuke.util.collections.SingleValueEnumeration;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManagerImpl;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.atomnuke.container.packaging.resource.ResourceUtil;

public class IdentityClassLoader extends ClassLoader {

   private static final Logger LOG = LoggerFactory.getLogger(IdentityClassLoader.class);

   private final ResourceManager resourceManager;
   private final ClassLoader parent;

   public IdentityClassLoader() {
      this(getSystemClassLoader(), new ResourceManagerImpl());
   }

   public IdentityClassLoader(ResourceManager resourceManager) {
      this(getSystemClassLoader(), resourceManager);
   }

   public IdentityClassLoader(ClassLoader parent, ResourceManager resourceManager) {
      super(parent);

      this.resourceManager = resourceManager;
      this.parent = parent;
   }

   @Override
   public Class<?> loadClass(String name) throws ClassNotFoundException {
      return loadClass(name, false);
   }

   @Override
   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class c = findLoadedClass(name);

      if (c == null) {
         try {
            if (parent != null) {
               c = parent.loadClass(name);
            } else {
               c = findSystemClass(name);
            }
         } catch (ClassNotFoundException e) {
            // ClassNotFoundException thrown if class not found
            // from the non-null parent class loader
            c = findClass(name);
         }

         if (c == null) {
            // If still not found throw an exception
            throw new ClassNotFoundException(name);
         }
      }

      if (resolve) {
         resolveClass(c);
      }

      return c;
   }

   @Override
   protected Class<?> findClass(String classPath) throws ClassNotFoundException {
      final Resource descriptor = resourceManager.lookup(classPath);

      if (descriptor != null) {
         try {
            return defineClass(descriptor);
         } catch (IOException ioe) {
            LOG.error("Failed to resolve registered class: " + classPath, ioe);
         }
      }

      return null;
   }

   //findResource("/META-INF/stuff")
   @Override
   protected URL findResource(String resourcePath) {
      URL resourceUrl = super.findResource(resourcePath);

      if (resourceUrl == null) {
         final Resource descriptor = resourceManager.lookup(resourcePath);

         if (descriptor != null) {
            resourceUrl = descriptor.url();
         } else if (parent instanceof IdentityClassLoader) {
            LOG.debug("Unable to find resource: " + resourcePath);
         }
      }

      return resourceUrl;
   }

   @Override
   protected Enumeration<URL> findResources(String name) throws IOException {
      final URL resourceUrl = findResource(name);

      return resourceUrl != null ? new SingleValueEnumeration<URL>(resourceUrl) : (Enumeration<URL>) EmptyEnumeration.instance();
   }

   @Override
   public URL getResource(String name) {
      return findResource(name);
   }

   @Override
   public Enumeration<URL> getResources(String name) throws IOException {
      return findResources(name);
   }

   final Class<?> defineClass(Resource descriptor) throws IOException {
      final URL resourceUrl = descriptor.url();

      if (resourceUrl == null) {
         return null;
      }

      final byte[] classBytes = readResource(resourceUrl);
      final String packageName = ResourceUtil.instance().relativePathToJavaPackage(descriptor.relativePath());

      if (getPackage(packageName) == null) {
         definePackage(packageName, null, null, null, null, null, null, null);
      }

      final String classPath = ResourceUtil.instance().relativePathToClassPath(descriptor.relativePath());
      return defineClass(classPath, classBytes, 0, classBytes.length);
   }

   private byte[] readResource(URL resourceUrl) throws IOException {
      final InputStream resourceInputStream = resourceUrl.openStream();

      return readResource(resourceInputStream).toByteArray();
   }

   public static ByteArrayOutputStream readResource(InputStream resourceInputStream) throws IOException {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      RawInputStreamReader.instance().copyTo(resourceInputStream, byteArrayOutputStream);
      resourceInputStream.close();

      return byteArrayOutputStream;
   }
}
