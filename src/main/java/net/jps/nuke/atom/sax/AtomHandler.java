package net.jps.nuke.atom.sax;

import java.net.URI;
import java.util.List;
import java.util.Stack;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.PersonConstruct;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.stax.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DefaultHandler {

   private static final ModelHelper m = new ModelHelper();
   
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
      push(new HandlerContext(element, builder));
   }

   private void push(HandlerContext context) {
      contextStack.push(context);
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

         case NAME:
         case EMAIL:
         case URI:
            startChildElement(currentElement);
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
      if (!expectingCharacters()) {
         return;
      }

      final HandlerContext target = pop();
      final HandlerContext targetParent = peek();
      final AtomElement targetElement = target.getElementDef();

      if (targetElement == AtomElement.NAME) {
         switch (targetParent.getElementDef()) {
            case AUTHOR:
            case CONTRIBUTOR:
               ((PersonConstructBuilder) targetParent.getBuilder()).setName(new String(ch, start, length));
               break;

            default:
               throw m.unexpectedElement(targetParent.getElementDef(), targetElement);
         }
      } else if (targetElement == AtomElement.URI) {
         switch (targetParent.getElementDef()) {
            case AUTHOR:
            case CONTRIBUTOR:
               ((PersonConstructBuilder) targetParent.getBuilder()).setUri(new String(ch, start, length));
               break;

            default:
               throw m.unexpectedElement(targetParent.getElementDef(), targetElement);
         }
      } else if ((targetElement == AtomElement.EMAIL)) {
         switch (targetParent.getElementDef()) {
            case AUTHOR:
            case CONTRIBUTOR:
               ((PersonConstructBuilder) targetParent.getBuilder()).setEmail(new String(ch, start, length));
               break;

            default:
               throw m.unexpectedElement(targetParent.getElementDef(), targetElement);
         }
      } else {
         push(target);
      }
   }

   public boolean expectingCharacters() {
      switch (peek().getElementDef()) {
         case NAME:
         case URI:
         case EMAIL:
            return true;
      }

      return false;
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

   private void startChildElement(AtomElement child) {
      final HandlerContext previous = peek();
      push(child, previous.getBuilder());
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

      final List personConstructList = m.getPersonConstructList(element, parentContext);
      personConstructList.add(personConstructBuilder.build());
   }
}
