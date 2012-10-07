package org.atomnuke.container.packaging;

import java.net.URI;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public interface DeployedPackage {

   URI archiveUri();

   ResourceManager resourceManager();
}
