package net.jps.nuke.atom.sax.handler;

import net.jps.nuke.atom.xml.ModelHelper;
import net.jps.nuke.atom.sax.attribute.AttributeScanner;
import net.jps.nuke.atom.sax.attribute.AttributeScannerDriver;
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
import net.jps.nuke.atom.model.builder.XmlDateConstructBuilder;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author zinic
 */
public class AtomHandler extends ReaderAwareHandler {

   public static final ModelHelper MODEL_HELPER = new ModelHelper();
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

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(asLocalName(qName, localName), AtomElement.ROOT_ELEMENTS);
      final AttributeScannerDriver attributeScannerDriver = new AttributeScannerDriver(attributes);

      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }

      switch (currentElement) {
         case FEED:
            startFeed(attributeScannerDriver);
            break;

         case ENTRY:
            startEntry(attributeScannerDriver);
            break;
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      final String characters = new String(ch, start, length).trim();

      switch (contextManager.peek().getElementDef()) {
         case NAME:
            contextManager.peek(PersonConstructBuilder.class).builder().setName(characters);
            break;

         case URI:
            contextManager.peek(PersonConstructBuilder.class).builder().setUri(characters);
            break;

         case EMAIL:
            contextManager.peek(PersonConstructBuilder.class).builder().setEmail(characters);
            break;

         case GENERATOR:
            contextManager.peek(GeneratorBuilder.class).builder().getValueBuilder().append(characters);
            break;

         case ID:
         case ICON:
         case LOGO:
            contextManager.peek(LangAwareTextElementBuilder.class).builder().appendValue(characters);
            break;

         case PUBLISHED:
         case UPDATED:
            contextManager.peek(XmlDateConstructBuilder.class).builder().getDateStringBuilder().append(characters);
            break;
      }
   }

   public ParserResult getResult() {
      return result;
   }

   protected void startFieldContentElement(AtomElement element) {
      final HandlerContext previous = contextManager.peek();
      contextManager.push(element, previous.builder());
   }

   private void startFeed(AttributeScannerDriver attributes) {
      final FeedBuilder feedBuilder = FeedBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               feedBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               feedBuilder.setLang(value);
            }
         }
      });

      contextManager.push(AtomElement.FEED, feedBuilder);
      delegateTo(new FeedHandler(this));
   }

   private void startEntry(AttributeScannerDriver attributes) {
      final EntryBuilder entryBuilder = EntryBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               entryBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               entryBuilder.setLang(value);
            }
         }
      });

      contextManager.push(AtomElement.ENTRY, entryBuilder);
      delegateTo(new EntryHandler(this));
   }

   protected void startPersonConstruct(AtomElement element, AttributeScannerDriver attributes) {
      final PersonConstructBuilder personConstructBuilder = PersonConstructBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               personConstructBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               personConstructBuilder.setLang(value);
            }
         }
      });

      contextManager.push(element, personConstructBuilder);
   }

   protected void startGenerator(AttributeScannerDriver attributes) {
      final GeneratorBuilder generatorBuilder = GeneratorBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               generatorBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               generatorBuilder.setLang(value);
            } else if ("uri".equals(attrName)) {
               generatorBuilder.setUri(value);
            } else if ("version".equals(attrName)) {
               generatorBuilder.setVersion(value);
            }
         }
      });

      contextManager.push(AtomElement.GENERATOR, generatorBuilder);
   }

   protected void startTextConstruct(AtomElement element, AttributeScannerDriver attributes) {
      final TextConstructBuilder textConstructBuilder = TextConstructBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               textConstructBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               textConstructBuilder.setLang(value);
            } else if ("type".equals(attrName)) {
               textConstructBuilder.setType(Type.findIgnoreCase(value));
            }
         }
      });

      contextManager.push(element, textConstructBuilder);
      delegateTo(new MixedContentHandler(textConstructBuilder.getValueBuilder(), this));
   }

   protected void startDateConstruct(AtomElement element, AttributeScannerDriver attributes) {
      final XmlDateConstructBuilder dateConstructBuilder = XmlDateConstructBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               dateConstructBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               dateConstructBuilder.setLang(value);
            }
         }
      });

      contextManager.push(element, dateConstructBuilder);
   }

   protected void startLink(AttributeScannerDriver attributes) {
      final LinkBuilder linkBuilder = LinkBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

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

      contextManager.push(AtomElement.LINK, linkBuilder);
   }

   protected void startCategory(AttributeScannerDriver attributes) {
      final CategoryBuilder categoryBuilder = CategoryBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

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

      contextManager.push(AtomElement.CATEGORY, categoryBuilder);
   }

   protected void startLangAwareTextElement(AtomElement element, AttributeScannerDriver attributes) {
      final LangAwareTextElementBuilder textElementBuilder = LangAwareTextElementBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               textElementBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               textElementBuilder.setLang(value);
            }
         }
      });

      contextManager.push(element, textElementBuilder);
   }
}
