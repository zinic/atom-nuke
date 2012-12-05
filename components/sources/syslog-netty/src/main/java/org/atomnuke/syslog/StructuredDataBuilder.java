package org.atomnuke.syslog;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author zinic
 */
public class StructuredDataBuilder implements StructuredDataElement {

   private final Map<String, String> sdParams;
   private String id;

   public StructuredDataBuilder() {
      sdParams = new HashMap<String, String>();
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setParam(String name, String value) {
      sdParams.put(name, value);
   }

   @Override
   public String id() {
      return id;
   }

   @Override
   public Set<String> paramNames() {
      return Collections.unmodifiableSet(sdParams.keySet());
   }

   @Override
   public String getValue(String paramName) {
      return sdParams.get(paramName);
   }
}
