package org.atomnuke.atom.io.reader.sax;

import java.net.URI;
import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.atom.model.Type;
import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.FeedBuilder;
import org.atomnuke.atom.model.builder.GeneratorBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;
import org.atomnuke.atom.model.builder.PersonConstructBuilder;
import org.atomnuke.atom.model.builder.DateConstructBuilder;
import org.atomnuke.atom.model.builder.IconBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.LogoBuilder;
import org.atomnuke.atom.model.builder.RightsBuilder;
import org.atomnuke.atom.model.builder.SimpleContentBuilder;
import org.atomnuke.atom.model.builder.SubtitleBuilder;
import org.atomnuke.atom.model.builder.SummaryBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.atom.model.builder.TypedContentBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;
import org.atomnuke.atom.model.builder.ValueBuilder;
import org.atomnuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DelegatingHandler {

   protected final DocumentContextManager contextManager;
   protected final SaxAtomReaderResult result;

   public AtomHandler(XMLReader xmlReader) {
      super(xmlReader);

      contextManager = new DocumentContextManager();
      result = new SaxAtomReaderResult();
   }

   protected AtomHandler(AtomHandler delegate) {
      super(delegate);

      contextManager = delegate.contextManager;
      result = delegate.result;
   }

   protected static boolean handleCommonElement(DelegatingHandler handler, DocumentContextManager contextManager, AtomElement currentElement, Attributes attributes) throws SAXException {
      switch (currentElement) {
         case ID:
            startSimpleContentElement(new IdBuilder(), contextManager, currentElement, attributes);
            break;

         case ICON:
            startSimpleContentElement(new IconBuilder(), contextManager, currentElement, attributes);
            break;

         case LOGO:
            startSimpleContentElement(new LogoBuilder(), contextManager, currentElement, attributes);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(contextManager, currentElement);
            break;

         case UPDATED:
            startDateConstruct(new UpdatedBuilder(), contextManager, currentElement, attributes);
            break;

         case RIGHTS:
            startTypedContent(new RightsBuilder(), handler, contextManager, currentElement, attributes);
            break;

         case TITLE:
            startTypedContent(new TitleBuilder(), handler, contextManager, currentElement, attributes);
            break;

         case SUBTITLE:
            startTypedContent(new SubtitleBuilder(), handler, contextManager, currentElement, attributes);
            break;

         case SUMMARY:
            startTypedContent(new SummaryBuilder(), handler, contextManager, currentElement, attributes);
            break;

         case AUTHOR:
            startPersonConstruct(new AuthorBuilder(), contextManager, currentElement, attributes);
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
      final AtomElement currentElement = AtomElement.find(localName, AtomElement.ROOT_ELEMENTS);

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

      final String trimmedString = trimSubstring(characters);

      switch (contextManager.peek().getElementDef()) {
         case NAME:
            contextManager.peek(PersonConstructBuilder.class).builder().setName(trimmedString);
            break;

         case URI:
            contextManager.peek(PersonConstructBuilder.class).builder().setUri(trimmedString);
            break;

         case EMAIL:
            contextManager.peek(PersonConstructBuilder.class).builder().setEmail(trimmedString);
            break;

         case GENERATOR:
            contextManager.peek(GeneratorBuilder.class).builder().appendValue(trimmedString);
            break;

         case ID:
         case ICON:
         case LOGO:
         case PUBLISHED:
         case UPDATED:
            contextManager.peek(ValueBuilder.class).builder().appendValue(trimmedString);
            break;
      }
   }

   public ReaderResult getResult() {
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

   protected static void startPersonConstruct(PersonConstructBuilder builderInstance, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      builderInstance.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      builderInstance.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(element, builderInstance);
   }

   protected static void startGenerator(DocumentContextManager contextManager, Attributes attributes) {
      final GeneratorBuilder generatorBuilder = new GeneratorBuilder();

      generatorBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      generatorBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      generatorBuilder.setUri(attributes.getValue(AtomAttributeConstants.URI));
      generatorBuilder.setVersion(attributes.getValue(AtomAttributeConstants.VERSION));

      contextManager.push(AtomElement.GENERATOR, generatorBuilder);
   }

   protected static void startTypedContent(TypedContentBuilder typedContentBuilder, DelegatingHandler self, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      typedContentBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      typedContentBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));
      typedContentBuilder.setType(toType(attributes.getValue(AtomAttributeConstants.TYPE)));

      contextManager.push(element, typedContentBuilder);
      self.delegateTo(new MixedContentHandler(typedContentBuilder, self));
   }

   protected static void startDateConstruct(DateConstructBuilder dateConstructBuilder, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
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

   protected static void startSimpleContentElement(SimpleContentBuilder simpleContentBuilder, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      simpleContentBuilder.setBase(toUri(attributes.getValue(AtomAttributeConstants.BASE)));
      simpleContentBuilder.setLang(attributes.getValue(AtomAttributeConstants.LANG));

      contextManager.push(element, simpleContentBuilder);
   }
}
