package net.jps.nuke.atom.model.builder;

import java.net.URI;
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

   protected XmlDateConstructBuilder() {
   }

   public Updated buildUpdted() {
      return this;
   }
   
   public Published buildPublished() {
      return this;
   }
   
   public void setXmlDateString(String dateString) {
      this.date = DatatypeConverter.parseDateTime(dateString);
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
