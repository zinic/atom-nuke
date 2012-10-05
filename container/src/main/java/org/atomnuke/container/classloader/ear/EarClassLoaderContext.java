package org.atomnuke.container.classloader.ear;

public interface EarClassLoaderContext {

    EarDescriptor getEarDescriptor();

    ResourceIdentityTreeClassLoader getClassLoader();
}
