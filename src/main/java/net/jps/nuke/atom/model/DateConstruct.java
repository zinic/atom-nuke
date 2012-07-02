package net.jps.nuke.atom.model;

import java.util.Calendar;

/**
 *
 * @author zinic
 */
public interface DateConstruct extends AtomCommonAtributes {

   /**
    * Transforms the internal representation of the DateConstruct into a Java
    * Calendar object.
    *
    * @return
    */
   Calendar asCalendar();

   /**
    * Returns the string representation of the date construct. This preserves the 
    * toString method for other features.
    * 
    * @return 
    */
   String asText();
}
