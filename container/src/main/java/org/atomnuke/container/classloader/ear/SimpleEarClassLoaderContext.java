package org.atomnuke.container.classloader.ear;


import java.io.File;
import org.atomnuke.container.classloader.ResourceIdentityTree;

public class SimpleEarClassLoaderContext implements EarClassLoaderContext {

   private final ResourceIdentityTreeClassLoader classloaderContext;
   private final EarDescriptor earDescriptor;

   public SimpleEarClassLoaderContext(ClassLoader absoluteParent, File deploymentRoot) {
      final ResourceIdentityTree resourceIdentityTree = new ResourceIdentityTree();

      classloaderContext = new ResourceIdentityTreeClassLoader(resourceIdentityTree, deploymentRoot, absoluteParent);
      earDescriptor = new EarDescriptor(resourceIdentityTree);
   }

   @Override
   public ResourceIdentityTreeClassLoader getClassLoader() {
      return classloaderContext;
   }

   @Override
   public EarDescriptor getEarDescriptor() {
      return earDescriptor;
   }
}
