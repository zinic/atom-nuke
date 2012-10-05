package org.atomnuke.container.classloader.archive;

import java.util.jar.Manifest;

public interface ArchiveEntryHelper {

    EntryAction nextJarEntry(ArchiveResource je);

    void newJarManifest(ArchiveResource name, Manifest manifest);

    void newClass(ArchiveResource name, byte[] classBytes);

    void newResource(ArchiveResource name, byte[] resourceBytes);
}
