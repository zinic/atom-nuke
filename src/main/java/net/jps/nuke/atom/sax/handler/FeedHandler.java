package net.jps.nuke.atom.sax.handler;

import java.net.URI;
import java.util.List;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Link;
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
import net.jps.nuke.atom.sax.InvalidElementException;
import net.jps.nuke.atom.sax.attribute.AttributeScanner;
import net.jps.nuke.atom.sax.attribute.AttributeScannerDriver;
import net.jps.nuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author zinic
 */
public class FeedHandler extends AtomHandler {

   public FeedHandler(AtomHandler delegate) {
      super(delegate);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(asLocalName(qName, localName), AtomElement.FEED_ELEMENTS);
      final AttributeScannerDriver attributeScannerDriver = new AttributeScannerDriver(attributes);

      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }

      switch (currentElement) {
         case ENTRY:
            startEntry(attributeScannerDriver);
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            startPersonConstruct(currentElement, attributeScannerDriver);
            break;

         case CATEGORY:
            startCategory(attributeScannerDriver);
            break;

         case LINK:
            startLink(attributeScannerDriver);
            break;

         case GENERATOR:
            startGenerator(attributeScannerDriver);
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

         case UPDATED:
            startDateConstruct(currentElement, attributeScannerDriver);
            break;

         case RIGHTS:
         case TITLE:
         case SUBTITLE:
         case SUMMARY:
            startTextConstruct(currentElement, attributeScannerDriver);
            break;
      }
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

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();
      final String elementEnding = asLocalName(qName, localName);

      if (!currentElement.name().equalsIgnoreCase(elementEnding)) {
         throw new InvalidElementException("Element: " + currentElement + " was not expected. Expecting: " + elementEnding);
      }

      switch (currentElement) {
         case FEED:
            endFeed();
            break;
            
         case AUTHOR:
            endAuthor();
            break;

         case CONTRIBUTOR:
            endContributor();
            break;

         case GENERATOR:
            endGenerator();
            break;

         case UPDATED:
            endUpdated();
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

         case RIGHTS:
            endRights();
            break;

         case TITLE:
            endTitle();
            break;

         case SUBTITLE:
            endSubtitle();
            break;

         case NAME:
         case URI:
         case EMAIL:
            contextManager.pop();
            break;
      }
   }

   private void endFeed() {
      final HandlerContext<FeedBuilder> feedBuilderContext = contextManager.pop(FeedBuilder.class);
      result.setFeedBuilder(feedBuilderContext.builder());
   }

   private void endAuthor() {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addAuthor(personContext.builder().buildAuthor());
   }

   private void endContributor() {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addContributor(personContext.builder().buildContributor());
   }

   private void endId() {
      final HandlerContext<LangAwareTextElementBuilder> idContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setId(idContext.builder().build());
   }

   private void endLogo() {
      final HandlerContext<LangAwareTextElementBuilder> logoContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setLogo(logoContext.builder().build());
   }

   private void endIcon() {
      final HandlerContext<LangAwareTextElementBuilder> iconContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setIcon(iconContext.builder().build());
   }

   private void endUpdated() {
      final HandlerContext<XmlDateConstructBuilder> updatedContext = contextManager.pop(XmlDateConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setUpdated(updatedContext.builder().buildUpdated());
   }

   private void endCategory() {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      final List<Category> categoryList = contextManager.peek(FeedBuilder.class).builder().categories();

      categoryList.add(category.builder().build());
   }

   private void endLink() {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      final List<Link> linkList = contextManager.peek(FeedBuilder.class).builder().links();

      linkList.add(category.builder().build());
   }

   private void endGenerator() {
      final HandlerContext<GeneratorBuilder> generatorContext = contextManager.pop(GeneratorBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setGenerator(generatorContext.builder().build());
   }

   private void endRights() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setRights(textConstructContext.builder().buildRights());
   }

   private void endTitle() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setTitle(textConstructContext.builder().buildTitle());;
   }

   private void endSubtitle() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setSubtitle(textConstructContext.builder().buildSubtitle());
   }
}
