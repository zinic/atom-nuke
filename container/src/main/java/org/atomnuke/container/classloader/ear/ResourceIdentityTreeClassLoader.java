package org.atomnuke.container.classloader.ear;

import com.rackspace.papi.commons.util.StringUtilities;
import org.atomnuke.container.classloader.ResourceDescriptor;
import org.atomnuke.container.classloader.ResourceIdentityTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.atomnuke.container.classloader.archive.ResourceType;

public class ResourceIdentityTreeClassLoader extends ClassLoader {

   private static final Logger LOG = LoggerFactory.getLogger(ResourceIdentityTreeClassLoader.class);
   
   private final ResourceIdentityTree classPathIdentityTree;
   private final ClassLoader parentClassLoader;
   private final File resourceRoot;

   public ResourceIdentityTreeClassLoader(ResourceIdentityTree classPathIdentityTree, File resourceRoot, ClassLoader parent) {
      super(parent);

      this.classPathIdentityTree = classPathIdentityTree;
      this.resourceRoot = resourceRoot;
      this.parentClassLoader = parent;
   }

   public ResourceIdentityTree resourceIdentityTree() {
      return classPathIdentityTree;
   }

   public boolean register(ResourceDescriptor resourceDescriptor) {
      if (!classPathIdentityTree.hasConflictingIdentity(resourceDescriptor)) {
         classPathIdentityTree.register(resourceDescriptor);

         return true;
      }

      return false;
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
            if (parentClassLoader != null) {
               c = parentClassLoader.loadClass(name);
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
      final String resourcePath = classPath.replaceAll("\\.", "/") + ".class";
      final ResourceDescriptor descriptor = classPathIdentityTree.resourceDescriptorFor(resourcePath);

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
         final ResourceDescriptor descriptor = classPathIdentityTree.resourceDescriptorFor(resourcePath);

         if (descriptor != null) {
            resourceUrl = descriptorToUrl(descriptor);
         } else if (parentClassLoader instanceof ResourceIdentityTreeClassLoader) {
            LOG.debug("Unable to find resource: " + resourcePath);
         }
      }

      return resourceUrl;
   }

   final Class<?> defineClass(ResourceDescriptor descriptor) throws IOException {
      final URL resourceUrl = descriptorToUrl(descriptor);

      if (resourceUrl == null) {
         return null;
      }

      final ByteArrayOutputStream out = createOutputStream(resourceUrl);

      final String packageName = StringUtilities.trim(descriptor.archiveEntry().path(), "/").replaceAll("/", ".");

      if (getPackage(packageName) == null) {
         definePackage(packageName, null, null, null, null, null, null, null);
      }

      final byte[] classBytes = out.toByteArray();

      return defineClass(packageName + "." + descriptor.archiveEntry().simpleName(), classBytes, 0, classBytes.length);
   }

   private ByteArrayOutputStream createOutputStream(URL resourceUrl) throws IOException {
      final InputStream resourceInputStream = resourceUrl.openStream();

      return createOutPutStream(resourceInputStream);
   }

   public static ByteArrayOutputStream createOutPutStream(InputStream resourceInputStream) throws IOException {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      final byte[] byteBuffer = new byte[1024];
      int read;

      while ((read = resourceInputStream.read(byteBuffer)) != -1) {
         byteArrayOutputStream.write(byteBuffer, 0, read);
      }

      resourceInputStream.close();
      return byteArrayOutputStream;
   }

   private URL descriptorToUrl(ResourceDescriptor descriptor) {
      final String urlString = descriptor.archiveEntry().resourceType() == ResourceType.JAR
              ? buildJarResourceUrl(descriptor)
              : buildFileResourceUrl(descriptor);
      try {
         return new URL(urlString);
      } catch (MalformedURLException murle) {
         LOG.error("Malformed URL in class loader. URL: " + urlString);
         return null;
      }
   }

   private String buildFileResourceUrl(ResourceDescriptor descriptor) {
      final StringBuilder urlBuffer = new StringBuilder();
      urlBuffer.append("file:").append(resourceRoot.getAbsolutePath()).append('/').append(descriptor.archiveEntry().fullName());

      return urlBuffer.toString();
   }

   private String buildJarResourceUrl(ResourceDescriptor descriptor) {
      final StringBuilder urlBuffer = new StringBuilder();
      urlBuffer.append("jar:").append(descriptor.archiveEntry().archiveLocation().toString()).append("!/").append(descriptor.archiveEntry().fullName());

      return urlBuffer.toString();
   }
}
