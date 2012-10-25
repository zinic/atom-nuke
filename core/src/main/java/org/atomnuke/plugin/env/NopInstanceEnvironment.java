package org.atomnuke.plugin.env;

import java.util.Collections;
import java.util.List;
import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public final class NopInstanceEnvironment implements Environment {

   private static final Environment DEFAULT_LOCAL_ENV = new NopInstanceEnvironment();

   public static Environment getInstance() {
      return DEFAULT_LOCAL_ENV;
   }

   @Override
   public List<Service> services() {
      return Collections.EMPTY_LIST;
   }

   @Override
   public <T> T instantiate(Class<T> interfaceType, String referenceName) throws ReferenceInstantiationException {
      try {
         return interfaceType.cast(Class.forName(referenceName).newInstance());
      } catch (Exception ex) {
         throw new ReferenceInstantiationException(ex);
      }
   }

   @Override
   public boolean hasReference(String referenceName) {
      try {
         Class.forName(referenceName);
         return true;
      } catch (ClassNotFoundException cnfe) {
         return false;
      }
   }

   @Override
   public void stepOut() {
   }

   @Override
   public void stepInto() {
   }
}
