package org.atomnuke.syslog;

import java.util.Calendar;
import java.util.Set;

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
   
   Set<? extends StructuredDataElement> structuredData();

   int version();
}
