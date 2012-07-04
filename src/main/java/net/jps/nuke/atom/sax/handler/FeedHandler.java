package net.jps.nuke.atom.sax.handler;

import java.util.List;
import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.GeneratorBuilder;
import net.jps.nuke.atom.model.builder.LangAwareTextElementBuilder;
import net.jps.nuke.atom.model.builder.LinkBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.TextConstructBuilder;
import net.jps.nuke.atom.model.builder.DateConstructBuilder;
import net.jps.nuke.atom.sax.HandlerContext;
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
      final AtomElement currentElement = AtomElement.find(asLocalName(qName, localName), AtomElement.FEED_ELEMENTS);

      if (currentElement == null) {
         return;
      }

      switch (currentElement) {
         case ENTRY:
            startEntry(this, contextManager, attributes);
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            startPersonConstruct(contextManager, currentElement, attributes);
            break;

         case CATEGORY:
            startCategory(contextManager, attributes);
            break;

         case LINK:
            startLink(contextManager, attributes);
            break;

         case GENERATOR:
            startGenerator(contextManager, attributes);
            break;

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
            startTextConstruct(this, contextManager, currentElement, attributes);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();
      final String elementEnding = asLocalName(qName, localName);

      if (!currentElement.elementName().equals(elementEnding)) {
         return;
      }

      switch (currentElement) {
         case FEED:
            endFeed(contextManager, result);
            break;
            
         case AUTHOR:
            endAuthor(contextManager);
            break;

         case CONTRIBUTOR:
            endContributor(contextManager);
            break;

         case GENERATOR:
            endGenerator(contextManager);
            break;

         case UPDATED:
            endUpdated(contextManager);
            break;

         case LINK:
            endLink(contextManager);
            break;

         case CATEGORY:
            endCategory(contextManager);
            break;

         case ID:
            endId(contextManager);
            break;

         case ICON:
            endIcon(contextManager);
            break;

         case LOGO:
            endLogo(contextManager);
            break;

         case RIGHTS:
            endRights(contextManager);
            break;

         case TITLE:
            endTitle(contextManager);
            break;

         case SUBTITLE:
            endSubtitle(contextManager);
            break;

         case NAME:
         case URI:
         case EMAIL:
            contextManager.pop();
            break;
      }
   }

   private static void endFeed(DocumentContextManager contextManager, ParserResultImpl result) {
      final HandlerContext<FeedBuilder> feedBuilderContext = contextManager.pop(FeedBuilder.class);
      result.setFeedBuilder(feedBuilderContext.builder());
   }

   private static void endAuthor(DocumentContextManager contextManager) {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addAuthor(personContext.builder().buildAuthor());
   }

   private static void endContributor(DocumentContextManager contextManager) {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addContributor(personContext.builder().buildContributor());
   }

   private static void endId(DocumentContextManager contextManager) {
      final HandlerContext<LangAwareTextElementBuilder> idContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setId(idContext.builder().build());
   }

   private static void endLogo(DocumentContextManager contextManager) {
      final HandlerContext<LangAwareTextElementBuilder> logoContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setLogo(logoContext.builder().build());
   }

   private static void endIcon(DocumentContextManager contextManager) {
      final HandlerContext<LangAwareTextElementBuilder> iconContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setIcon(iconContext.builder().build());
   }

   private static void endUpdated(DocumentContextManager contextManager) {
      final HandlerContext<DateConstructBuilder> updatedContext = contextManager.pop(DateConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setUpdated(updatedContext.builder().buildUpdated());
   }

   private static void endCategory(DocumentContextManager contextManager) {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      final List<Category> categoryList = contextManager.peek(FeedBuilder.class).builder().categories();

      categoryList.add(category.builder().build());
   }

   private static void endLink(DocumentContextManager contextManager) {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      final List<Link> linkList = contextManager.peek(FeedBuilder.class).builder().links();

      linkList.add(category.builder().build());
   }

   private static void endGenerator(DocumentContextManager contextManager) {
      final HandlerContext<GeneratorBuilder> generatorContext = contextManager.pop(GeneratorBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setGenerator(generatorContext.builder().build());
   }

   private static void endRights(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setRights(textConstructContext.builder().buildRights());
   }

   private static void endTitle(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setTitle(textConstructContext.builder().buildTitle());;
   }

   private static void endSubtitle(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setSubtitle(textConstructContext.builder().buildSubtitle());
   }
}
