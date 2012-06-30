package net.jps.nuke.atom.sax;

import java.net.URI;
import java.util.List;
import java.util.Stack;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.ContentBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.LangAwareTextElementBuilder;
import net.jps.nuke.atom.model.builder.LinkBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.XmlDateConstructBuilder;
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

         case LINK:
            startLink(attributeScannerDriver);
            break;

         case ID:
         case ICON:
         case LOGO:
            startLangAwareTextElement(currentElement, attributeScannerDriver);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(currentElement);
            break;

         case PUBLISHED:
         case UPDATED:
            startDateConstruct(currentElement, attributeScannerDriver);
            break;
      }
   }

   public boolean expectingCharacters() {
      switch (peek().getElementDef()) {
         case NAME:
         case URI:
         case EMAIL:
         case CONTENT:
         case ID:
         case ICON:
         case PUBLISHED:
         case UPDATED:
         case LOGO:
            return true;
      }

      return false;
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

         case ID:
         case ICON:
         case LOGO:
            peek(LangAwareTextElementBuilder.class).builder().appendValue(characters);
            break;

         case PUBLISHED:
         case UPDATED:
            peek(XmlDateConstructBuilder.class).builder().appendDateString(characters);
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

         case PUBLISHED:
            endPublished();
            break;

         case UPDATED:
            endUpdated();
            break;

         case CONTENT:
            endContent();
            break;

         case LINK:
            endLink();
            break;

         case CATEGORY:
            endCategory();
            break;

         case ID:
            endId();
            break;

         case ICON:
            endIcon();
            break;

         case LOGO:
            endLogo();
            break;
      }
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

   private void startDateConstruct(AtomElement element, AttributeScannerDriver attributes) {
      final XmlDateConstructBuilder dateConstructBuilder = XmlDateConstructBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               dateConstructBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               dateConstructBuilder.setLang(value);
            }
         }
      });

      push(element, dateConstructBuilder);
   }

   private void startLink(AttributeScannerDriver attributes) {
      final LinkBuilder linkBuilder = LinkBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               linkBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               linkBuilder.setLang(value);
            } else if ("href".equals(attrName)) {
               linkBuilder.setHref(value);
            } else if ("hreflang".equals(attrName)) {
               linkBuilder.setHreflang(value);
            } else if ("length".equals(attrName)) {
               linkBuilder.setLength(Integer.parseInt(value));
            } else if ("rel".equals(attrName)) {
               linkBuilder.setRel(value);
            } else if ("title".equals(attrName)) {
               linkBuilder.setTitle(value);
            } else if ("type".equals(attrName)) {
               linkBuilder.setType(value);
            }
         }
      });

      push(AtomElement.LINK, linkBuilder);
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

   private void startLangAwareTextElement(AtomElement element, AttributeScannerDriver attributes) {
      final LangAwareTextElementBuilder textElementBuilder = LangAwareTextElementBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = simplifyLocalName(qname, localName);

            if ("base".equals(attrName)) {
               textElementBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               textElementBuilder.setLang(value);
            }
         }
      });

      push(element, textElementBuilder);
   }

   private void startFieldContentElement(AtomElement element) {
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

   private void endId() {
      final HandlerContext<LangAwareTextElementBuilder> idContext = pop(LangAwareTextElementBuilder.class);
      final HandlerContext parent = peek();

      switch (parent.getElementDef()) {
         case FEED:
            ((FeedBuilder) parent.builder()).setId(idContext.builder().build());
            break;

         case ENTRY:
            ((EntryBuilder) parent.builder()).setId(idContext.builder().build());
            break;

         case SOURCE:
         default:
            throw _.unexpectedElement(idContext.getElementDef());
      }
   }

   private void endLogo() {
      final HandlerContext<LangAwareTextElementBuilder> logoContext = pop(LangAwareTextElementBuilder.class);
      final HandlerContext parent = peek();

      switch (parent.getElementDef()) {
         case FEED:
            ((FeedBuilder) parent.builder()).setLogo(logoContext.builder().build());
            break;

         case SOURCE:
         default:
            throw _.unexpectedElement(logoContext.getElementDef());
      }
   }

   private void endIcon() {
      final HandlerContext<LangAwareTextElementBuilder> iconContext = pop(LangAwareTextElementBuilder.class);
      final HandlerContext parent = peek();

      switch (parent.getElementDef()) {
         case FEED:
            ((FeedBuilder) parent.builder()).setIcon(iconContext.builder().build());
            break;

         case SOURCE:
         default:
            throw _.unexpectedElement(iconContext.getElementDef());
      }
   }

   private void endUpdated() {
      final HandlerContext<XmlDateConstructBuilder> updatedContext = pop(XmlDateConstructBuilder.class);
      final HandlerContext parent = peek();

      switch (parent.getElementDef()) {
         case FEED:
            ((FeedBuilder) parent.builder()).setUpdated(updatedContext.builder().buildUpdated());
            break;
            
         case ENTRY:
            ((EntryBuilder) parent.builder()).setUpdated(updatedContext.builder().buildUpdated());
            break;

         case SOURCE:
         default:
            throw _.unexpectedElement(updatedContext.getElementDef());
      }
   }

   private void endPublished() {
      final HandlerContext<XmlDateConstructBuilder> publishedContext = pop(XmlDateConstructBuilder.class);
      peek(EntryBuilder.class).builder().setPublished(publishedContext.builder().buildPublished());
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

   private void endLink() {
      final HandlerContext<LinkBuilder> category = pop(LinkBuilder.class);
      final List<Link> linkList = _.getLinkList(peek());

      linkList.add(category.builder().build());
   }
}
