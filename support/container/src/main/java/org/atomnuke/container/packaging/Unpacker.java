package org.atomnuke.container.packaging;

import java.net.URI;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public interface Unpacker {

   DeployedPackage unpack(ResourceManager resourceManager, URI archiveLocation) throws UnpackerException;

   boolean canUnpack(URI archiveLocation);
}
