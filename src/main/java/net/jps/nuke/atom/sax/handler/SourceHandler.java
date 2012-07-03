package net.jps.nuke.atom.sax.handler;

import java.util.List;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.GeneratorBuilder;
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
public class SourceHandler extends AtomHandler {
   
   public SourceHandler(AtomHandler delegate) {
      super(delegate);
   }
   
   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.findIgnoreCase(asLocalName(qName, localName), AtomElement.SOURCE_ELEMENTS);
      
      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }
      
      switch (currentElement) {
         case AUTHOR:
            startPersonConstruct(currentElement, attributes);
            break;
         
         case CATEGORY:
            startCategory(attributes);
            break;
         
         case LINK:
            startLink(attributes);
            break;
         
         case GENERATOR:
            startGenerator(attributes);
            break;
         
         case ID:
         case ICON:
         case LOGO:
            startLangAwareTextElement(currentElement, attributes);
            break;
         
         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(currentElement);
            break;
         
         case UPDATED:
            startDateConstruct(currentElement, attributes);
            break;
         
         case RIGHTS:
         case TITLE:
         case SUBTITLE:
         case SUMMARY:
            startTextConstruct(currentElement, attributes);
            break;
      }
   }
   
   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();
      final String elementEnding = asLocalName(qName, localName);
      
      if (!currentElement.name().equalsIgnoreCase(elementEnding)) {
         return;
//         throw new InvalidElementException("Element: " + currentElement + " was not expected. Expecting: " + elementEnding);
      }
      
      switch (currentElement) {
         case SOURCE:
            endSource();
            break;
         
         case AUTHOR:
            endAuthor();
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
   
   private void endSource() {
      final HandlerContext<SourceBuilder> sourceBuilder = contextManager.pop(SourceBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setSource(sourceBuilder.builder().build());
      
      releaseToParent();
   }
   
   private void endAuthor() {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().addAuthor(personContext.builder().buildAuthor());
   }
   
   private void endId() {
      final HandlerContext<LangAwareTextElementBuilder> idContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setId(idContext.builder().build());
   }
   
   private void endLogo() {
      final HandlerContext<LangAwareTextElementBuilder> logoContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setLogo(logoContext.builder().build());
   }
   
   private void endIcon() {
      final HandlerContext<LangAwareTextElementBuilder> iconContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setIcon(iconContext.builder().build());
   }
   
   private void endUpdated() {
      final HandlerContext<XmlDateConstructBuilder> updatedContext = contextManager.pop(XmlDateConstructBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setUpdated(updatedContext.builder().buildUpdated());
   }
   
   private void endCategory() {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      final List<Category> categoryList = contextManager.peek(SourceBuilder.class).builder().categories();
      
      categoryList.add(category.builder().build());
   }
   
   private void endLink() {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      final List<Link> linkList = contextManager.peek(SourceBuilder.class).builder().links();
      
      linkList.add(category.builder().build());
   }
   
   private void endGenerator() {
      final HandlerContext<GeneratorBuilder> generatorContext = contextManager.pop(GeneratorBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setGenerator(generatorContext.builder().build());
   }
   
   private void endRights() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setRights(textConstructContext.builder().buildRights());
   }
   
   private void endTitle() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setTitle(textConstructContext.builder().buildTitle());
   }
   
   private void endSubtitle() {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setSubtitle(textConstructContext.builder().buildSubtitle());
   }
}
