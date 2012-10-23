package org.atomnuke.servlet;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.atomnuke.atom.io.AtomReadException;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;
import org.atomnuke.source.QueueSource;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.io.LimitedReadInputStream;
import org.atomnuke.util.io.ReadLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.http.HttpStatus;

/**
 *
 * @author zinic
 */
public class AtomSinkServlet extends HttpServlet {

   private static final Logger LOG = LoggerFactory.getLogger(AtomSinkServlet.class);
   private static final DatatypeFactory DATATYPE_FACTORY;

   static {
      try {
         DATATYPE_FACTORY = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException dce) {
         LOG.error(dce.getMessage(), dce);

         throw new RuntimeException("Unable to initialize a datatype factory for XML dates.");
      }
   }

   private final AtomReaderFactory atomReaderFactory;
   private final QueueSource queueSource;

   public AtomSinkServlet(QueueSource queueSource) throws InitializationException {
      this.queueSource = queueSource;

      atomReaderFactory = new SaxAtomReaderFactory();
   }

   private static void notAllowed(HttpServletResponse resp) {
      resp.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
      resp.addHeader("ALLOW", "POST");
   }

   private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      final String contentType = req.getHeader("Content-Type");

      if (contentType == null || !contentType.equalsIgnoreCase("application/atom+xml")) {
         resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
      } else {
         try {
            final ReaderResult readerResult = atomReaderFactory.getInstance().read(new LimitedReadInputStream(req.getInputStream(), 5242880));

            if (readerResult.getEntry() != null) {
               final XMLGregorianCalendar cal = DATATYPE_FACTORY.newXMLGregorianCalendar((GregorianCalendar) GregorianCalendar.getInstance());
               final EntryBuilder newEntry = new EntryBuilder(readerResult.getEntry());

               newEntry.setId(new IdBuilder().setValue(UUID.randomUUID().toString()).build());
               newEntry.setUpdated(new UpdatedBuilder().setValue(cal.toXMLFormat()).build());

               queueSource.put(newEntry.build());
               resp.setStatus(HttpStatus.ACCEPTED_202);
            } else {
               resp.setStatus(HttpStatus.BAD_REQUEST_400);
               resp.getWriter().append("Request entity must be an Entry.");
            }
         } catch (AtomReadException ape) {
            LOG.error(ape.getMessage(), ape);

            final Throwable cause = ape.getCause();

            if (cause instanceof ReadLimitException) {
               resp.setStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE_413);
               resp.getWriter().append("Request entity too large. The limit is 5MB.");
            } else if (cause instanceof IOException) {
               resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
               resp.getWriter().append("Unable to read request entity. Reason: " + cause.getMessage());
            } else {
               resp.setStatus(HttpStatus.BAD_REQUEST_400);
               resp.getWriter().append("Request entity not well formed. Reason: " + ape.getMessage());
            }
         }
      }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req, resp);
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req, resp);
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      notAllowed(resp);
   }

   @Override
   protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      notAllowed(resp);
   }

   @Override
   protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      notAllowed(resp);
   }

   @Override
   protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      notAllowed(resp);
   }

   @Override
   protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      notAllowed(resp);
   }
}
