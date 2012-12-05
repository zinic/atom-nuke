package org.atomnuke.syslog;

import java.util.Calendar;

/**
 *
 * @author zinic
 */
public interface SyslogMessage {

   /*
    * Set to -1 when there is no priority for a message
    */
   int priority();

   /*
    * RFC3339 Timestamp
    */
   Calendar timestamp();

   String originHostname();

   String applicationName();

   String processId();

   String content();
   
   String messageId();
   
   StructuredDataElement structuredData();

   int version();
}
