package net.jps.nuke.atom.sax;

import java.net.URI;
import java.util.List;
import java.util.Stack;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.ContentBuilder;
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

   private static final ModelHelper _ = new ModelHelper();
   private final Stack<HandlerContext<?>> contextStack;
   private final ParserResultImpl result;

   public AtomHandler() {
      contextStack = new Stack<HandlerContext<?>>();
      result = new ParserResultImpl();
   }

   private boolean contextStackHasElements() {
      return !contextStack.empty();
   }

   private HandlerContext<?> peek() {
      return contextStack.peek();
   }

   private HandlerContext<?> pop() {
      return contextStack.pop();
   }

   private <T> HandlerContext<T> peek(Class<T> castAs) {
      final HandlerContext<?> entry = peek();

      if (castAs.isAssignableFrom(entry.builder().getClass())) {
         return (HandlerContext<T>) entry;
      }

      throw new IllegalArgumentException("Class: " + castAs.getName() + " is not a superclass or superinterface of: " + entry.builder().getClass().getName());
   }

   private <T> HandlerContext<T> pop(Class<T> castAs) {
      final HandlerContext<?> entry = pop();

      if (castAs.isAssignableFrom(entry.builder().getClass())) {
         return (HandlerContext<T>) entry;
      }

      throw new IllegalArgumentException("Class: " + castAs.getName() + " is not a superclass or superinterface of: " + entry.builder().getClass().getName());
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

         case CONTENT:
            startContent(currentElement, attributeScannerDriver);
            break;
            
         case CATEGORY:
            startCategory(attributeScannerDriver);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startContentField(currentElement);
            break;
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      if (!expectingCharacters()) {
         return;
      }

      final String characters = new String(ch, start, length).trim();

      switch (peek().getElementDef()) {
         case CONTENT:
            peek(ContentBuilder.class).builder().appendValue(characters);
            break;

         case NAME:
            pop();
            peek(PersonConstructBuilder.class).builder().setName(characters);
            break;

         case URI:
            pop();
            peek(PersonConstructBuilder.class).builder().setUri(characters);
            break;

         case EMAIL:
            pop();
            peek(PersonConstructBuilder.class).builder().setEmail(characters);
            break;

         default:
            throw _.invalidState(peek().getElementDef(), "Not expecting content for element.");
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
            endPersonConstruct();
            break;

         case CONTENT:
            endContent();
            break;
            
         case CATEGORY:
            endCategory();
            break;
      }
   }

   public boolean expectingCharacters() {
      switch (peek().getElementDef()) {
         case NAME:
         case URI:
         case EMAIL:
         case CONTENT:
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

   private void startPersonConstruct(AtomElement element, AttributeScannerDriver attributes) {
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

      push(element, personConstructBuilder);
   }

   private void startContent(AtomElement element, AttributeScannerDriver attributes) {
      final ContentBuilder contentBuilder = ContentBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               contentBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               contentBuilder.setLang(value);
            } else if ("type".equals(attrName)) {
               contentBuilder.setType(value);
            } else if ("src".equals(attrName)) {
               contentBuilder.setSrc(value);
            }
         }
      });

      push(element, contentBuilder);
   }

   private void startCategory(AttributeScannerDriver attributes) {
      final CategoryBuilder categoryBuilder = CategoryBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               categoryBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               categoryBuilder.setLang(value);
            } else if ("scheme".equals(attrName)) {
               categoryBuilder.setScheme(value);
            } else if ("term".equals(attrName)) {
               categoryBuilder.setTerm(value);
            } else if ("label".equals(attrName)) {
               categoryBuilder.setLabel(value);
            }
         }
      });

      push(AtomElement.CATEGORY, categoryBuilder);
   }

   private void startContentField(AtomElement element) {
      final HandlerContext previous = peek();
      push(element, previous.builder());
   }

   private void endFeed() {
      final HandlerContext<FeedBuilder> feedBuilderContext = pop(FeedBuilder.class);
      result.setFeedBuilder(feedBuilderContext.builder());
   }

   private void endEntry() {
      final HandlerContext<EntryBuilder> entryContext = pop(EntryBuilder.class);

      if (contextStackHasElements()) {
         final HandlerContext<FeedBuilder> feedBuilderContext = peek(FeedBuilder.class);
         feedBuilderContext.builder().addEntry(entryContext.builder().build());
      } else {
         result.setEntryBuilder(entryContext.builder());
      }
   }

   private void endPersonConstruct() {
      final HandlerContext<PersonConstructBuilder> personContext = pop(PersonConstructBuilder.class);
      final HandlerContext parent = peek();
      
      switch (personContext.getElementDef()) {
         case CONTRIBUTOR:
            _.getContributorList(parent).add(personContext.builder().buildContributor());
            break;
            
         case AUTHOR:
            _.getAuthorList(parent).add(personContext.builder().buildAuthor());
            break;
            
         default:
            throw _.unexpectedElement(personContext.getElementDef());
      }
   }

   private void endContent() {
      final HandlerContext<ContentBuilder> content = pop(ContentBuilder.class);
      peek(EntryBuilder.class).builder().setContent(content.builder().build());
   }

   private void endCategory() {
      final HandlerContext<CategoryBuilder> category = pop(CategoryBuilder.class);
      final List<Category> categoryList = _.getCategoryList(peek());
      
      categoryList.add(category.builder().build());
   }
}
