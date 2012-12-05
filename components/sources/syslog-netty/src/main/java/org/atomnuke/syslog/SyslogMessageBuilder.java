package org.atomnuke.syslog;

import java.util.Calendar;

/**
 *
 * @author zinic
 */
public class SyslogMessageBuilder implements SyslogMessage {

   private final StructuredDataBuilder structuredDataBuilder;
   
   private String originHostname, applicationName, processId, messageId, content;
   private int priority, version;
   private Calendar timestamp;

   public SyslogMessageBuilder() {
      structuredDataBuilder = new StructuredDataBuilder();
   }

   public StructuredDataBuilder getStructuredDataBuilder() {
      return structuredDataBuilder;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public void setVersion(int version) {
      this.version = version;
   }

   public void setOriginHostname(String originHostname) {
      this.originHostname = originHostname;
   }

   public void setApplicationName(String applicationName) {
      this.applicationName = applicationName;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public void setProcessId(String processId) {
      this.processId = processId;
   }

   public void setTimestamp(Calendar timestamp) {
      this.timestamp = timestamp;
   }

   public void setMessageId(String messageId) {
      this.messageId = messageId;
   }

   @Override
   public String content() {
      return content;
   }

   @Override
   public StructuredDataElement structuredData() {
      return structuredDataBuilder;
   }

   @Override
   public String messageId() {
      return messageId;
   }

   @Override
   public int version() {
      return version;
   }

   @Override
   public int priority() {
      return priority;
   }

   @Override
   public Calendar timestamp() {
      return timestamp;
   }

   @Override
   public String originHostname() {
      return originHostname;
   }

   @Override
   public String applicationName() {
      return applicationName;
   }

   @Override
   public String processId() {
      return processId;
   }
}
