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
public class DateConstructImpl extends SimpleContent implements DateConstruct, Updated, Published {

   @Override
   public Calendar toCalendar() {
      return DatatypeConverter.parseDate(toString());
   }
}
