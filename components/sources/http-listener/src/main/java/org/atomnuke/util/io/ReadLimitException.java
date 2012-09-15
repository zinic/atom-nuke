package org.atomnuke.util.io;

import java.io.IOException;

/**
 *
 * @author zinic
 */
public class ReadLimitException extends IOException {

   public ReadLimitException(String message) {
      super(message);
   }
}
