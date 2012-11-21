package org.atomnuke.context;

import java.util.Map;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.service.introspection.ServicesInterrogator;

/**
 *
 * @author zinic
 */
public interface Context {

   NukeEnvironment environment();

   ServicesInterrogator services();

   /**
    *
    * @return
    */
   Map<String, String> parameters();
}
