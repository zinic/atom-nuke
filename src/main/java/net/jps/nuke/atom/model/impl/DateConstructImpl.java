package net.jps.nuke.atom.model.impl;

import java.util.Calendar;
import net.jps.nuke.atom.model.DateConstruct;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Updated;

/**
 *
 * @author zinic
 */
public abstract class DateConstructImpl extends AtomCommonAttributesImpl implements DateConstruct, Updated, Published {

   protected Calendar date;

   public Calendar asCalendar() {
      return date;
   }
}
