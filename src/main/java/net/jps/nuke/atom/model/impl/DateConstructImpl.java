package net.jps.nuke.atom.model.impl;

import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import net.jps.nuke.atom.model.DateConstruct;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Updated;

/**
 *
 * @author zinic
 */
public abstract class DateConstructImpl extends AtomCommonAttributesImpl implements DateConstruct, Updated, Published {

   protected StringBuilder dateStringBuilder;
   private Calendar date;

   public Calendar asCalendar() {
      if (date == null) {
         date = DatatypeConverter.parseDate(dateStringBuilder.toString());
      }

      return date;
   }

   public String asText() {
      return dateStringBuilder.toString();
   }
}
