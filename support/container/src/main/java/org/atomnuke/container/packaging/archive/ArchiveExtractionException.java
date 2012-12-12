package org.atomnuke.container.packaging.archive;

import java.io.IOException;

/**
 *
 * @author zinic
 */
public class ArchiveExtractionException extends IOException {

   public ArchiveExtractionException(String message) {
      super(message);
   }

   public ArchiveExtractionException(String message, Throwable cause) {
      super(message, cause);
   }

   public ArchiveExtractionException(Throwable cause) {
      super(cause);
   }
}
