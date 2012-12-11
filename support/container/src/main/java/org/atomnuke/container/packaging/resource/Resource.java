package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.net.URL;
import org.atomnuke.container.packaging.archive.ResourceType;

/**
 *
 * @author zinic
 */
public interface Resource {

   URI uri();

   URL url();

   byte[] digestBytes();

   String relativePath();
   
   ResourceType type();
}
