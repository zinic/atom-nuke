package org.atomnuke.plugin.local;

import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class LocalInstanceEnvironment implements Environment {

   private static final Environment DEFAULT_LOCAL_ENV = new LocalInstanceEnvironment();

   public static Environment getInstance() {
      return DEFAULT_LOCAL_ENV;
   }

   private LocalInstanceEnvironment() {
   }

   @Override
   public void stepOut() {
   }

   @Override
   public void stepInto() {
   }
}
