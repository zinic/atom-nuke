package org.atomnuke.atom.io.reader.impl.sax;

import org.atomnuke.atom.ParserResultImpl;
import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContributorBuilder;
import org.atomnuke.atom.model.builder.FeedBuilder;
import org.atomnuke.atom.model.builder.GeneratorBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.IconBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.LogoBuilder;
import org.atomnuke.atom.model.builder.RightsBuilder;
import org.atomnuke.atom.model.builder.SubtitleBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;
import org.atomnuke.atom.sax.DocumentContextManager;
import org.atomnuke.atom.sax.HandlerContext;
import org.atomnuke.atom.xml.AtomElement;
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
      final AtomElement currentElement = AtomElement.find(localName, AtomElement.FEED_ELEMENTS);

      if (currentElement == null || handleCommonElement(this, contextManager, currentElement, attributes)) {
         return;
      }

      switch (currentElement) {
         case ENTRY:
            startEntry(this, contextManager, attributes);
            break;

         case CONTRIBUTOR:
            startPersonConstruct(new ContributorBuilder(), contextManager, currentElement, attributes);
            break;

         case UPDATED:
            startDateConstruct(new UpdatedBuilder(), contextManager, currentElement, attributes);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();

      if (!currentElement.elementName().equals(localName)) {
         return;
      }

      switch (currentElement) {
         case FEED:
            endFeed(contextManager, result);
            break;

         case ENTRY:
            endEntry(contextManager);
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

   private static void endEntry(DocumentContextManager contextManager) {
      final HandlerContext<EntryBuilder> entryContext = contextManager.pop(EntryBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addEntry(entryContext.builder().build());
   }

   private static void endAuthor(DocumentContextManager contextManager) {
      final HandlerContext<AuthorBuilder> authorContext = contextManager.pop(AuthorBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addAuthor(authorContext.builder().build());
   }

   private static void endContributor(DocumentContextManager contextManager) {
      final HandlerContext<ContributorBuilder> personContext = contextManager.pop(ContributorBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addContributor(personContext.builder().build());
   }

   private static void endId(DocumentContextManager contextManager) {
      final HandlerContext<IdBuilder> idContext = contextManager.pop(IdBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setId(idContext.builder().build());
   }

   private static void endLogo(DocumentContextManager contextManager) {
      final HandlerContext<LogoBuilder> logoContext = contextManager.pop(LogoBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setLogo(logoContext.builder().build());
   }

   private static void endIcon(DocumentContextManager contextManager) {
      final HandlerContext<IconBuilder> iconContext = contextManager.pop(IconBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setIcon(iconContext.builder().build());
   }

   private static void endUpdated(DocumentContextManager contextManager) {
      final HandlerContext<UpdatedBuilder> updatedContext = contextManager.pop(UpdatedBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setUpdated(updatedContext.builder().build());
   }

   private static void endCategory(DocumentContextManager contextManager) {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addCategory(category.builder().build());
   }

   private static void endLink(DocumentContextManager contextManager) {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().addLink(category.builder().build());
   }

   private static void endGenerator(DocumentContextManager contextManager) {
      final HandlerContext<GeneratorBuilder> generatorContext = contextManager.pop(GeneratorBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setGenerator(generatorContext.builder().build());
   }

   private static void endRights(DocumentContextManager contextManager) {
      final HandlerContext<RightsBuilder> textConstructContext = contextManager.pop(RightsBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setRights(textConstructContext.builder().build());
   }

   private static void endTitle(DocumentContextManager contextManager) {
      final HandlerContext<TitleBuilder> textConstructContext = contextManager.pop(TitleBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setTitle(textConstructContext.builder().build());
   }

   private static void endSubtitle(DocumentContextManager contextManager) {
      final HandlerContext<SubtitleBuilder> textConstructContext = contextManager.pop(SubtitleBuilder.class);
      contextManager.peek(FeedBuilder.class).builder().setSubtitle(textConstructContext.builder().build());
   }
}
