package net.jps.nuke.atom.sax.handler;

import net.jps.nuke.atom.sax.attribute.AttributeScanner;
import net.jps.nuke.atom.sax.attribute.AttributeScannerDriver;
import java.net.URI;
import java.util.List;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.ContentBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.LangAwareTextElementBuilder;
import net.jps.nuke.atom.model.builder.LinkBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.SourceBuilder;
import net.jps.nuke.atom.model.builder.TextConstructBuilder;
import net.jps.nuke.atom.model.builder.XmlDateConstructBuilder;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.sax.InvalidElementException;
import net.jps.nuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author zinic
 */
public class EntryHandler extends AtomHandler {

   public EntryHandler(AtomHandler delegate) {
      super(delegate);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(asLocalName(qName, localName), AtomElement.ENTRY_ELEMENTS);
      final AttributeScannerDriver attributeScannerDriver = new AttributeScannerDriver(attributes);

      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }

      switch (currentElement) {
         case SOURCE:
            startSource(attributeScannerDriver);
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

         case RIGHTS:
         case TITLE:
         case SUMMARY:
            startTextConstruct(currentElement, attributeScannerDriver);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();
      final String elementEnding = asLocalName(qName, localName);

      if (!currentElement.name().equalsIgnoreCase(elementEnding)) {
         throw new InvalidElementException("Element: " + currentElement + " was not expected. Expecting: " + elementEnding);
      }

      switch (currentElement) {
         case ENTRY:
            endEntry();
            break;

         case AUTHOR:
            endAuthor();
            break;

         case CONTRIBUTOR:
            endContributor();
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

         case RIGHTS:
            endRights();
            break;

         case TITLE:
            endTitle();
            break;

         case SUMMARY:
            endSummary();
            break;

         case NAME:
         case URI:
         case EMAIL:
            contextManager.pop();
            break;
      }
   }

   private void startSource(AttributeScannerDriver attributes) {
      final SourceBuilder sourceBuilder = SourceBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

            if ("base".equals(attrName)) {
               sourceBuilder.setBase(URI.create(value));
            } else if ("lang".equals(attrName)) {
               sourceBuilder.setLang(value);
            }
         }
      });

      contextManager.push(AtomElement.SOURCE, sourceBuilder);
      delegateTo(new SourceHandler(this));
   }

   private void startContent(AtomElement element, AttributeScannerDriver attributes) {
      final ContentBuilder contentBuilder = ContentBuilder.newBuilder();

      attributes.scan(new AttributeScanner() {
         public void attribute(String localName, String qname, String value) {
            final String attrName = asLocalName(qname, localName);

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

      contextManager.push(element, contentBuilder);
      delegateTo(new MixedContentHandler(contentBuilder.getValueBuilder(), this));
   }

   private void endEntry() {
      final HandlerContext<EntryBuilder> entryContext = contextManager.pop(EntryBuilder.class);

      if (contextManager.hasContext()) {
         final HandlerContext<FeedBuilder> feedBuilderContext = contextManager.peek(FeedBuilder.class);
         feedBuilderContext.builder().addEntry(entryContext.builder().build());
      } else {
         result.setEntryBuilder(entryContext.builder());
      }

      releaseToParent();
   }

   private void endAuthor() {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addAuthor(personContext.builder().buildAuthor());
   }

   private void endContributor() {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addContributor(personContext.builder().buildContributor());
   }

   private void endId() {
      final HandlerContext<LangAwareTextElementBuilder> idContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setId(idContext.builder().build());
   }

   private void endUpdated() {
      final HandlerContext<XmlDateConstructBuilder> updatedContext = contextManager.pop(XmlDateConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setUpdated(updatedContext.builder().buildUpdated());
   }

   private void endPublished() {
      final HandlerContext<XmlDateConstructBuilder> publishedContext = contextManager.pop(XmlDateConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setPublished(publishedContext.builder().buildPublished());
   }

   private void endContent() {
      final HandlerContext<ContentBuilder> content = contextManager.pop(ContentBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setContent(content.builder().build());
   }

   private void endCategory() {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      final List<Category> categoryList = contextManager.peek(EntryBuilder.class).builder().categories();

      categoryList.add(category.builder().build());
   }

   private void endLink() {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      final List<Link> linkList = contextManager.peek(EntryBuilder.class).builder().links();

      linkList.add(category.builder().build());
   }

   private void endRights() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setRights(textConstructContext.builder().buildRights());
   }

   private void endSummary() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setSummary(textConstructContext.builder().buildSummary());
   }

   private void endTitle() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setTitle(textConstructContext.builder().buildTitle());
   }
}
