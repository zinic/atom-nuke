package org.atomnuke.service;

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
