package org.atomnuke.container.classloader.ear;

import org.atomnuke.container.classloader.archive.ArchiveEntryHelper;

public interface EarArchiveEntryHelper extends ArchiveEntryHelper {

    EarClassLoaderContext getClassLoaderContext();
}
