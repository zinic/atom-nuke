package org.atomnuke.atom.model;

import java.net.URI;
import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContentBuilder;
import org.atomnuke.atom.model.builder.ContributorBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.GeneratorBuilder;
import org.atomnuke.atom.model.builder.IconBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;
import org.atomnuke.atom.model.builder.LogoBuilder;
import org.atomnuke.atom.model.builder.PublishedBuilder;
import org.atomnuke.atom.model.builder.RightsBuilder;
import org.atomnuke.atom.model.builder.SubtitleBuilder;
import org.atomnuke.atom.model.builder.SummaryBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;

/**
 *
 * @author zinic
 */
public final class ModelTestUtil {

   public static final String URI_STRING = "/uri", LANG = "en", EMAIL = "email_user@user.domain", PERSON_NAME = "John Hopper";
   public static final URI URI_INSTANCE = URI.create(URI_STRING);

   private ModelTestUtil() {
   }

   public static Entry newEntry() {
      final EntryBuilder newEntry = new EntryBuilder().setBase(URI_INSTANCE).setLang(LANG).setContent(newContent())
              .setId(newId()).setPublished(newPublished()).setRights(newRights()).setSummary(newSummary())
              .setTitle(newTitle()).setUpdated(newUpdated());

      newEntry.addAuthor(newAuthor());
      newEntry.addCategory(newCategory());
      newEntry.addContributor(newContributor());
      newEntry.addLink(newLink());

      return newEntry.build();
   }

   public static Link newLink() {
      return new LinkBuilder().setBase(URI_INSTANCE).setLang(LANG).setHref(URI_STRING).setHreflang(LANG).setLength(27).setRel("self").setTitle("Link title").setType("text").build();
   }

   public static Category newCategory() {
      return new CategoryBuilder().setBase(URI_INSTANCE).setLang(LANG).setLabel("label").setScheme("scheme").setTerm("category a").build();
   }

   public static Content newContent() {
      return new ContentBuilder().setBase(URI_INSTANCE).setLang(LANG).setSrc("src").setType("text").setValue("Content Value").build();
   }

   public static Contributor newContributor() {
      return new ContributorBuilder().setBase(URI_INSTANCE).setLang(LANG).setEmail(EMAIL).setName(PERSON_NAME).setUri(URI_STRING).build();
   }

   public static Author newAuthor() {
      return new AuthorBuilder().setBase(URI_INSTANCE).setLang(LANG).setEmail(EMAIL).setName(PERSON_NAME).setUri(URI_STRING).build();
   }

   public static Generator newGenerator() {
      return new GeneratorBuilder().setBase(URI_INSTANCE).setLang(LANG).setUri(URI_STRING).setValue("Generator Value").setVersion("1.0").build();
   }

   public static Icon newIcon() {
      return new IconBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("Icon").build();
   }

   public static Id newId() {
      return new IdBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("ID").build();
   }

   public static Logo newLogo() {
      return new LogoBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("Logo").build();
   }

   public static Rights newRights() {
      return new RightsBuilder().setBase(URI_INSTANCE).setLang(LANG).setType(Type.TEXT).setValue("Rights").build();
   }

   public static Subtitle newSubtitle() {
      return new SubtitleBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("Subtitle").build();
   }

   public static Title newTitle() {
      return new TitleBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("Title").build();
   }

   public static Summary newSummary() {
      return new SummaryBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("Summary").build();
   }

   public static Updated newUpdated() {
      return new UpdatedBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("2012-07-04 12:12:21 CDT").build();
   }

   public static Published newPublished() {
      return new PublishedBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("2012-07-04 12:12:21 CDT").build();
   }
}
