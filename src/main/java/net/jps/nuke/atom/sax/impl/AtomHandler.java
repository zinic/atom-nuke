package net.jps.nuke.atom.sax.impl;

import java.net.URI;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.Type;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.GeneratorBuilder;
import net.jps.nuke.atom.model.builder.LangAwareTextElementBuilder;
import net.jps.nuke.atom.model.builder.LinkBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.TextConstructBuilder;
import net.jps.nuke.atom.model.builder.DateConstructBuilder;
import net.jps.nuke.atom.sax.DocumentContextManager;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.sax.DelegatingHandler;
import net.jps.nuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DelegatingHandler {

   protected final DocumentContextManager contextManager;
   protected final ParserResultImpl result;

   public AtomHandler(XMLReader xmlReader) {
      super(xmlReader);

      contextManager = new DocumentContextManager();
      result = new ParserResultImpl();
   }

   public AtomHandler(AtomHandler delegate) {
      super(delegate);

      contextManager = delegate.contextManager;
      result = delegate.result;
   }

   protected static String asLocalName(String qName, String localName) {
      return "".equals(localName) ? qName : localName;
   }

   protected static boolean handleCommonElement(DelegatingHandler handler, DocumentContextManager contextManager, AtomElement currentElement, Attributes attributes) throws SAXException {
      switch (currentElement) {
         case ID:
         case ICON:
         case LOGO:
            startLangAwareTextElement(contextManager, currentElement, attributes);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(contextManager, currentElement);
            break;

         case UPDATED:
            startDateConstruct(contextManager, currentElement, attributes);
            break;

         case RIGHTS:
         case TITLE:
         case SUBTITLE:
         case SUMMARY:
            startTextConstruct(handler, contextManager, currentElement, attributes);
            break;

         case AUTHOR:
            startPersonConstruct(contextManager, currentElement, attributes);
            break;

         case CATEGORY:
            startCategory(contextManager, attributes);
            break;

         case GENERATOR:
            startGenerator(contextManager, attributes);
            break;

         case LINK:
            startLink(contextManager, attributes);
            break;

         default:
            return false;
      }

      return true;
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.find(asLocalName(qName, localName), AtomElement.ROOT_ELEMENTS);

      if (currentElement == null) {
         return;
      }

      switch (currentElement) {
         case FEED:
            startFeed(this, contextManager, attributes);
            break;

         case ENTRY:
            startEntry(this, contextManager, attributes);
            break;
      }
   }

   protected static String trimSubstring(StringBuilder sb) {
      int first, last;

      for (first = 0; first < sb.length(); first++) {
         if (!Character.isWhitespace(sb.charAt(first))) {
            break;
         }
      }

      for (last = sb.length(); last > first; last--) {
         if (!Character.isWhitespace(sb.charAt(last - 1))) {
            break;
         }
      }

      return sb.substring(first, last);
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      final StringBuilder characters = new StringBuilder();
      characters.append(ch, start, length);

      switch (contextManager.peek().getElementDef()) {
         case NAME:
            contextManager.peek(PersonConstructBuilder.class).builder().setName(trimSubstring(characters));
            break;

         case URI:
            contextManager.peek(PersonConstructBuilder.class).builder().setUri(trimSubstring(characters));
            break;

         case EMAIL:
            contextManager.peek(PersonConstructBuilder.class).builder().setEmail(trimSubstring(characters));
            break;

         case GENERATOR:
            contextManager.peek(GeneratorBuilder.class).builder().appendValue(trimSubstring(characters));
            break;

         case ID:
         case ICON:
         case LOGO:
            contextManager.peek(LangAwareTextElementBuilder.class).builder().appendValue(trimSubstring(characters));
            break;

         case PUBLISHED:
         case UPDATED:
            contextManager.peek(DateConstructBuilder.class).builder().getDateStringBuilder().append(trimSubstring(characters));
            break;
      }
   }

   public ParserResult getResult() {
      return result;
   }

   /**
    * Null safe.
    *
    * @param st
    * @return
    */
   protected static URI toUri(String st) {
      return st == null ? null : URI.create(st);
   }

   /**
    * Null safe.
    *
    * @param st
    * @return
    */
   protected static Integer toInteger(String st) {
      return st == null ? null : Integer.parseInt(st);
   }

   /**
    * Null safe.
    *
    * @param st
    * @return
    */
   protected static Type toType(String st) {
      return st == null ? null : Type.find(st);
   }

   protected static void startFieldContentElement(DocumentContextManager contextManager, AtomElement element) {
      final HandlerContext previous = contextManager.peek();
      contextManager.push(element, previous.builder());
   }

   private static void startFeed(AtomHandler self, DocumentContextManager contextManager, Attributes attributes) {
      final FeedBuilder feedBuilder = new FeedBuilder();

      feedBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      feedBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(AtomElement.FEED, feedBuilder);
      self.delegateTo(new FeedHandler(self));
   }

   protected static void startEntry(AtomHandler self, DocumentContextManager contextManager, Attributes attributes) {
      final EntryBuilder entryBuilder = new EntryBuilder();

      entryBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      entryBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(AtomElement.ENTRY, entryBuilder);
      self.delegateTo(new EntryHandler(self));
   }

   protected static void startPersonConstruct(DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      final PersonConstructBuilder personConstructBuilder = new PersonConstructBuilder();

      personConstructBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      personConstructBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(element, personConstructBuilder);
   }

   protected static void startGenerator(DocumentContextManager contextManager, Attributes attributes) {
      final GeneratorBuilder generatorBuilder = new GeneratorBuilder();

      generatorBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      generatorBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      generatorBuilder.setUri(attributes.getValue(AtomAttributeConstants.URI));
      generatorBuilder.setVersion(attributes.getValue(AtomAttributeConstants.VERSION));

      contextManager.push(AtomElement.GENERATOR, generatorBuilder);
   }

   protected static void startTextConstruct(DelegatingHandler self, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      final TextConstructBuilder textConstructBuilder = new TextConstructBuilder();

      textConstructBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      textConstructBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      textConstructBuilder.setType(toType(attributes.getValue(AtomAttributeConstants.TYPE)));

      contextManager.push(element, textConstructBuilder);
      self.delegateTo(new MixedContentHandler(textConstructBuilder, self));
   }

   protected static void startDateConstruct(DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      final DateConstructBuilder dateConstructBuilder = new DateConstructBuilder();

      dateConstructBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      dateConstructBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(element, dateConstructBuilder);
   }

   protected static void startLink(DocumentContextManager contextManager, Attributes attributes) {
      final LinkBuilder linkBuilder = new LinkBuilder();

      linkBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      linkBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      linkBuilder.setHref(attributes.getValue(AtomAttributeConstants.HREF));
      linkBuilder.setHreflang(attributes.getValue(AtomAttributeConstants.HREFLANG));
      linkBuilder.setRel(attributes.getValue(AtomAttributeConstants.REL));
      linkBuilder.setTitle(attributes.getValue(AtomAttributeConstants.TITLE));
      linkBuilder.setType(attributes.getValue(AtomAttributeConstants.TYPE));
      linkBuilder.setLength(toInteger(attributes.getValue(AtomAttributeConstants.LENGTH)));

      contextManager.push(AtomElement.LINK, linkBuilder);
   }

   protected static void startCategory(DocumentContextManager contextManager, Attributes attributes) {
      final CategoryBuilder categoryBuilder = new CategoryBuilder();

      categoryBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      categoryBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      categoryBuilder.setScheme(attributes.getValue(AtomAttributeConstants.SCHEME));
      categoryBuilder.setTerm(attributes.getValue(AtomAttributeConstants.TERM));
      categoryBuilder.setLabel(attributes.getValue(AtomAttributeConstants.LABEL));

      contextManager.push(AtomElement.CATEGORY, categoryBuilder);
   }

   protected static void startLangAwareTextElement(DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      final LangAwareTextElementBuilder textElementBuilder = new LangAwareTextElementBuilder();

      textElementBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      textElementBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(element, textElementBuilder);
   }
}
