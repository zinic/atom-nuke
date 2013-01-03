package org.atomnuke.collectd.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.FastDateFormat;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContentBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.PublishedBuilder;
import org.atomnuke.collectd.command.PutValCommand;
import org.atomnuke.collectd.command.PutValParser;
import org.atomnuke.util.source.QueueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class CollectdSinkServlet extends HttpServlet {

   private static final Logger LOG = LoggerFactory.getLogger(CollectdSinkServlet.class);

   private static final String JSON_CONTENT_TEMPLATE = "{\"timestamp\" : \"$\", \"value\" : \"$\"}";

   private static final String COLLECTD_PLUGIN_SCHEME = "collectd.stats.plugin";
   private static final String COLLECTD_TYPE_SCHEME = "collectd.stats.type";

   private static final String COLLECTD_SCHEME = "collectd";
   private static final String PATH_SEPERATOR = "/";

   private final QueueSource queueSource;
   private final boolean debug;

   public CollectdSinkServlet(QueueSource queueSource, boolean debug) {
      this.queueSource = queueSource;
      this.debug = debug;
   }

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream()));

      String line;

      while ((line = bufferedReader.readLine()) != null) {
         final PutValCommand parsedValue = PutValParser.instance().parse(line);

         if (parsedValue == null) {
            LOG.debug("Bad putval command: " + line);
            continue;
         }

         final EntryBuilder entryBuilder = new EntryBuilder();

         // Set the ID
         entryBuilder.setId(new IdBuilder().setValue(UUID.randomUUID().toString()).build());

         // Put in the proper categories
         // Plugin category
         entryBuilder.addCategory(new CategoryBuilder().setScheme(COLLECTD_PLUGIN_SCHEME).setTerm(parsedValue.plugin()).build());

         // Type category
         entryBuilder.addCategory(new CategoryBuilder().setScheme(COLLECTD_TYPE_SCHEME).setTerm(parsedValue.type()).build());

         // Metrics category
         entryBuilder.addCategory(buildMetricsCat(parsedValue));

         // Set the publication time
         entryBuilder.setPublished(new PublishedBuilder().setValue(FastDateFormat.getInstance().format(Calendar.getInstance())).build());

         // Set the content
         entryBuilder.setContent(new ContentBuilder()
                 .setType("application/json")
                 .setValue(JSON_CONTENT_TEMPLATE.replaceFirst("\\$", parsedValue.timestamp()).replaceFirst("\\$", parsedValue.value())).build());

         // Queue the entry up
         queueSource.put(entryBuilder.build());
      }

      bufferedReader.close();
   }

   private Category buildMetricsCat(PutValCommand parsedValue) {
      final StringBuilder catBuilder = new StringBuilder(parsedValue.host());
      catBuilder.append(PATH_SEPERATOR).append(parsedValue.type());

      if (parsedValue.pluginInstance() != null) {
         catBuilder.append(PATH_SEPERATOR).append(parsedValue.pluginInstance());
      }

      if (parsedValue.typeInstance() != null) {
         catBuilder.append(PATH_SEPERATOR).append(parsedValue.typeInstance());
      }

      if (debug) {
         LOG.info("Emitting: " + COLLECTD_SCHEME + "." + catBuilder.toString() + " - Value: " + parsedValue.value());
      }

      return new CategoryBuilder().setScheme(COLLECTD_SCHEME).setTerm(catBuilder.toString()).build();
   }
}
