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
   Calendar toCalendar();
}
