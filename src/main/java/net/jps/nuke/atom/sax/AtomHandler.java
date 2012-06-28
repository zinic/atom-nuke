package net.jps.nuke.atom.sax;

import java.net.URI;
import java.net.URISyntaxException;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.stax.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DefaultHandler {

   private final ParserResultImpl result;

   
   
   public AtomHandler() {
      result = new ParserResultImpl();
   }

   @Override
   public void startDocument() throws SAXException {
      super.startDocument();
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      switch (AtomElement.valueOf(localName)) {
         case FEED:
         case ENTRY:
         case CONTENT:
         case AUTHOR:
         case CATEGORY:
         case CONTRIBUTOR:
         case GENERATOR:
         case ICON:
         case ID:
         case LINK:
         case LOGO:
         case PUBLISHED:
         case RIGHTS:
         case SOURCE:
         case SUBTITLE:
         case SUMMARY:
         case TITLE:
         case UPDATED:
      }
   }

   @Override
   public void endDocument() throws SAXException {
      super.endDocument();
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      super.endElement(uri, localName, qName);
   }

   public ParserResult getResult() {
      return result;
   }
}
