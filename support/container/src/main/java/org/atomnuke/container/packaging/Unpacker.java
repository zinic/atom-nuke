package org.atomnuke.container.packaging;

import java.net.URI;

/**
 *
 * @author zinic
 */
public interface Unpacker {

   DeployedPackage unpack(URI archiveLocation) throws UnpackerException;

   boolean canUnpack(URI archiveLocation);
}
