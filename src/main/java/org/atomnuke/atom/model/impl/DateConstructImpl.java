package org.atomnuke.atom.model.impl;

import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import org.atomnuke.atom.model.DateConstruct;
import org.atomnuke.atom.model.Published;
import org.atomnuke.atom.model.Updated;

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
