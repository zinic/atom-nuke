package org.atomnuke.syslog;

import java.util.Set;

/**
 *
 * @author zinic
 */
public interface StructuredDataElement {

   String id();

   Set<String> paramNames();

   String getValue(String paramName);
}
