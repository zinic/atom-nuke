package net.jps.nuke.atom.model.builder;

import java.net.URI;
import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Updated;
import net.jps.nuke.atom.model.impl.DateConstructImpl;

/**
 *
 * @author zinic
 */
public class XmlDateConstructBuilder extends DateConstructImpl {

   public static XmlDateConstructBuilder newBuilder() {
      return new XmlDateConstructBuilder();
   }
   private StringBuilder dateStringBuilder;

   protected XmlDateConstructBuilder() {
      dateStringBuilder = null;
   }

   public Published buildPublished() {
      date = DatatypeConverter.parseDate(dateStringBuilder.toString());

      return this;
   }

   public Updated buildUpdated() {
      date = DatatypeConverter.parseDate(dateStringBuilder.toString());

      return this;
   }

   public void appendDateString(String dateString) {
      if (dateStringBuilder == null) {
         dateStringBuilder = new StringBuilder();
      }

      dateStringBuilder.append(dateString);
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
