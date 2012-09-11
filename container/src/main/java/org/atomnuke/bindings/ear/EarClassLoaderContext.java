package org.atomnuke.bindings.ear;

import com.rackspace.papi.commons.util.classloader.ear.EarClassLoader;

public interface EarClassLoaderContext {

    EarDescriptor getEarDescriptor();

    EarClassLoader getClassLoader();
}
