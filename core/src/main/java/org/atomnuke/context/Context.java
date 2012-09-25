package org.atomnuke.context;

import java.util.Map;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public interface Context {

   ServiceManager services();

   /**
    *
    * @return
    */
   Map<String, String> parameters();
}
