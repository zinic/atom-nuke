package org.atomnuke.context;

import java.util.Map;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public interface Context {

   NukeEnvironment environment();

   ServiceManager services();

   /**
    *
    * @return
    */
   Map<String, String> parameters();
}
