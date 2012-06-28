package net.jps.nuke.atom.sax;

import java.net.URI;
import java.util.Stack;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.SourceBuilder;
import net.jps.nuke.atom.stax.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DefaultHandler {

   private final Stack<HandlerContext<?>> contextStack;
   private final ParserResultImpl result;

   public AtomHandler() {
      contextStack = new Stack<HandlerContext<?>>();
      result = new ParserResultImpl();
   }

   private boolean contextStackHasElements() {
      return !contextStack.empty();
   }

   private <T> HandlerContext<T> peek() {
      return (HandlerContext<T>) contextStack.peek();
   }

   private <T> HandlerContext<T> pop() {
      return (HandlerContext<T>) contextStack.pop();
   }

   private void push(AtomElement element, Object builder) {
      contextStack.push(new HandlerContext(element, builder));
   }

   private static String simplifyLocalName(String qName, String localName) {
      return "".equals(localName) ? qName : localName;
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(simplifyLocalName(qName, localName));
      final AttributeScannerDriver attributeScannerDriver = new AttributeScannerDriver(attributes);

      if (currentElement == null) {
         System.out.println("URI: " + uri + "  Local Name: " + localName + "  QName: " + qName);
         return;
      }

      switch (currentElement) {
         case FEED:
            startFeed(attributeScannerDriver);
            break;

         case ENTRY:
            startEntry(attributeScannerDriver);
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            startPersonConstruct(currentElement, attributeScannerDriver);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(simplifyLocalName(qName, localName));

      switch (currentElement) {
         case FEED:
            endFeed();
            break;

         case ENTRY:
            endEntry();
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            endPersonConstruct(currentElement);
            break;
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      super.characters(ch, start, length);
   }

   public ParserResult getResult() {
      return result;
   }

   private void startFeed(AttributeScannerDriver attributes) {
      final FeedBuilder feedBuilder = FeedBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               feedBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               feedBuilder.setLang(value);
            }
         }
      });

      push(AtomElement.FEED, feedBuilder);
   }

   private void startEntry(AttributeScannerDriver attributes) {
      final EntryBuilder entryBuilder = EntryBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               entryBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               entryBuilder.setLang(value);
            }
         }
      });

      push(AtomElement.ENTRY, entryBuilder);
   }

   private void startPersonConstruct(AtomElement personConstructElement, AttributeScannerDriver attributes) {
      final PersonConstructBuilder personConstructBuilder = PersonConstructBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               personConstructBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               personConstructBuilder.setLang(value);
            }
         }
      });

      push(personConstructElement, personConstructBuilder);
   }

   private void endFeed() {
      final HandlerContext<FeedBuilder> feedBuilderContext = pop();
      result.setFeedBuilder(feedBuilderContext.getBuilder());
   }

   private void endEntry() {
      final HandlerContext<EntryBuilder> entryContext = pop();

      if (contextStackHasElements()) {
         final HandlerContext<FeedBuilder> feedBuilderContext = peek();
         feedBuilderContext.getBuilder().addEntry(entryContext.getBuilder().build());
      } else {
         result.setEntryBuilder(entryContext.getBuilder());
      }
   }

   private void endPersonConstruct(AtomElement element) {
      final HandlerContext<PersonConstructBuilder> personConstructContext = pop();
      final HandlerContext parentContext = peek();

      final PersonConstructBuilder personConstructBuilder = personConstructContext.getBuilder();

      if (element == AtomElement.AUTHOR) {
         final Author author = personConstructBuilder.buildAuthor();

         switch (parentContext.getElementDef()) {
            case FEED:
               ((HandlerContext<FeedBuilder>) parentContext).getBuilder().addAuthor(author);
               break;

            case ENTRY:
               ((HandlerContext<EntryBuilder>) parentContext).getBuilder().addAuthor(author);
               break;

            case SOURCE:
               ((HandlerContext<SourceBuilder>) parentContext).getBuilder().addAuthor(author);
               break;

            default:
               throw new RuntimeException("Unexpected parent element: " + parentContext.getElementDef() + " on element: " + element);
         }
      } else {
         final Contributor contributor = personConstructBuilder.buildContributor();

         switch (parentContext.getElementDef()) {
            case FEED:
               ((HandlerContext<FeedBuilder>) parentContext).getBuilder().addContributor(contributor);
               break;

            case ENTRY:
               ((HandlerContext<EntryBuilder>) parentContext).getBuilder().addContributor(contributor);
               break;

            default:
               throw new RuntimeException("Unexpected parent element: " + parentContext.getElementDef() + " on element: " + element);
         }
      }
   }
}
