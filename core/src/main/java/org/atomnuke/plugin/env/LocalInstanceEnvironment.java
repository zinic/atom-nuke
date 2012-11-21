package org.atomnuke.plugin.env;

import java.util.Collections;
import java.util.List;
import org.atomnuke.plugin.Environment;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public class LocalInstanceEnvironment extends ClassLoaderEnvironment {

   public static Environment getInstance() {
      return new LocalInstanceEnvironment(Thread.currentThread().getContextClassLoader());
   }

   public LocalInstanceEnvironment(ClassLoader classLoader) {
      super(classLoader);
   }

   @Override
   public List<Service> services() {
      return Collections.EMPTY_LIST;
   }

   @Override
   public String toString() {
      return "LocalInstanceEnvironment{" + super.toString() + '}';
   }


}
