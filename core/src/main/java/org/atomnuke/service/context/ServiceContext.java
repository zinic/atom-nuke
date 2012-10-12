package org.atomnuke.service.context;

import java.util.Map;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public interface ServiceContext {

   ServiceManager manager();

   /**
    *
    * @return
    */
   Map<String, String> parameters();
}
