package org.atomnuke.container.packaging.resource;

import java.net.URI;
import org.atomnuke.container.packaging.archive.ResourceType;

/**
 *
 * @author zinic
 */
public interface Resource {

   URI location();

   byte[] digestBytes();

   String relativePath();

   ResourceType type();
}
