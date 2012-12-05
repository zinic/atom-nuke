package org.atomnuke.syslog.parser;

/**
 *
 * @author zinic
 */
public class SyslogParserException extends Exception {

   public SyslogParserException(String message) {
      super(message);
   }

   public SyslogParserException(String message, Throwable cause) {
      super(message, cause);
   }

   public SyslogParserException(Throwable cause) {
      super(cause);
   }
}
