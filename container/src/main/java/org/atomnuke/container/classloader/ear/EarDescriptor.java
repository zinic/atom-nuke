package org.atomnuke.container.classloader.ear;

import org.atomnuke.container.classloader.ResourceIdentityTree;


public class EarDescriptor {

   private final ResourceIdentityTree rid;
   private String applicationName;

   public EarDescriptor(ResourceIdentityTree rid) {
      applicationName = "";

      this.rid = rid;
   }

   public ResourceIdentityTree resourceIdentityTree() {
      return rid;
   }

   public void setApplicationName(String applicationName) {
      this.applicationName = applicationName;
   }

   public String getApplicationName() {
      return applicationName;
   }
}
