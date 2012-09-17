package org.atomnuke.container.service.construct.reflection;

import org.atomnuke.container.service.construct.ConstructionException;
import org.atomnuke.container.service.construct.Constructor;

/**
 *
 * @author zinic
 */
public class GenericReflectionConstructor implements Constructor {

   public GenericReflectionConstructor() {
   }

   @Override
   public <T> T constructInstance(Class<T> instanceClass) throws ConstructionException {
      try {
         return instanceClass.newInstance();
      } catch (IllegalAccessException iae) {
         throw new ConstructionException(iae);
      } catch (InstantiationException ie) {
         throw new ConstructionException("Building instance of: " + instanceClass + " failed. Reason: " + ie.getMessage(), ie);
      }
   }
}
