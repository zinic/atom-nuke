package org.atomnuke.atom.model;

import java.util.Calendar;
import org.atomnuke.atom.model.annotation.ContentConstraint;
import org.atomnuke.atom.model.constraint.XmlDateConstraint;

/**
 *
 * @author zinic
 */
@ContentConstraint(XmlDateConstraint.class)
public interface DateConstruct extends SimpleContent {

   /**
    * Transforms the internal representation of the DateConstruct into a Java
    * Calendar object.
    *
    * @return
    */
   Calendar toCalendar();
}
