package org.atomnuke.service.context;

import java.util.Map;

/**
 *
 * @author zinic
 */
public class ServiceContextImpl implements ServiceContext {

   private final Map<String, String> parameters;

   public ServiceContextImpl(Map<String, String> parameters) {
      this.parameters = parameters;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }
}
