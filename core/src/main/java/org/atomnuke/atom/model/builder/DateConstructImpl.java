package org.atomnuke.atom.model.builder;

import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import org.atomnuke.atom.model.DateConstruct;
import org.atomnuke.atom.model.Published;
import org.atomnuke.atom.model.Updated;

/**
 *
 * @author zinic
 */
class DateConstructImpl extends SimpleContentImpl implements DateConstruct, Updated, Published {

   @Override
   public Calendar toCalendar() {
      return DatatypeConverter.parseDate(toString());
   }
}
