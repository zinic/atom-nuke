package org.atomnuke.bindings.ear;

import com.rackspace.papi.commons.util.plugin.archive.ArchiveEntryListener;

public interface EarArchiveEntryListener extends ArchiveEntryListener {

    EarClassLoaderContext getClassLoaderContext();
}
