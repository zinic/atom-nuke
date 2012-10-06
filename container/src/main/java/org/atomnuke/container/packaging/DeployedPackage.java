package org.atomnuke.container.packaging;

import java.net.URI;
import org.atomnuke.container.classloader.ResourceRegistry;

/**
 *
 * @author zinic
 */
public interface DeployedPackage {

   URI archiveUri();

   ResourceRegistry resourceRegistry();
}
