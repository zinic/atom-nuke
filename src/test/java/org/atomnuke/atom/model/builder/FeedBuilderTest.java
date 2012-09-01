package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Type;
import org.atomnuke.atom.model.Updated;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class FeedBuilderTest {

   private static final String URI_STRING = "/uri", LANG = "en";
   private static final URI URI_INSTANCE = URI.create(URI_STRING);

   public static Generator newGenerator() {
      return new GeneratorBuilder().setBase(URI_INSTANCE).setLang(LANG).setUri(URI_STRING).setValue("Generator Value").setVersion("1.0").build();
   }

   public static Icon newIcon() {
      return new IconBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("ICON").build();
   }

   public static Id newId() {
      return new IdBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("ID").build();
   }

   public static Logo newLogo() {
      return new LogoBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("LOGO").build();
   }

   public static Rights newRights() {
      return new RightsBuilder().setBase(URI_INSTANCE).setLang(LANG).setType(Type.TEXT).setValue("LOGO").build();
   }

   public static Subtitle newSubtitle() {
      return new SubtitleBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("LOGO").build();
   }

   public static Title newTitle() {
      return new TitleBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("LOGO").build();
   }

   public static Updated newUpdated() {
      return new UpdatedBuilder().setBase(URI_INSTANCE).setLang(LANG).setValue("LOGO").build();
   }

   public static class WhenCopyingFeedObjects {

      @Test
      public void shouldCopy() {
         final FeedBuilder builder = new FeedBuilder();

         builder.setBase(URI.create("/uri")).setLang("en");
         builder.setGenerator(newGenerator()).setIcon(newIcon()).setId(newId()).setLogo(newLogo());
         builder.setRights(newRights()).setSubtitle(newSubtitle()).setTitle(newTitle()).setUpdated(newUpdated());

         final Feed original = builder.build();
         final Feed copy = new FeedBuilder(original).build();

         // TODO:Test - Finish asserts beyong my own little sanity check
         assertEquals(copy.generator().uri(), original.generator().uri());
         assertEquals(copy.generator().version(), original.generator().version());
         assertEquals(copy.generator().base(), original.generator().base());
         assertEquals(copy.generator().lang(), original.generator().lang());
         assertEquals(copy.generator().toString(), original.generator().toString());
      }
   }
}
