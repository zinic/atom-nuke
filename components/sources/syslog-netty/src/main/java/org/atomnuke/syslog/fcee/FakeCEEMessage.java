package org.atomnuke.syslog.fcee;

/**
 *
 * @author zinic
 */
public class FakeCEEMessage {

   private String application;
   private String host;
   private String content;

   public String getApplication() {
      return application;
   }

   public void setApplication(String application) {
      this.application = application;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }
}
