package org.atomnuke.service.jetty.version;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class VersionServlet extends HttpServlet {

   private static final Logger LOGGER = LoggerFactory.getLogger(VersionServlet.class);
   private static final String GET_METHOD = "GET";
   
   private final String versionString;

   public VersionServlet() {
      final URL resourceURL = VersionServlet.class.getResource("build-version.txt");
      String actualVersion = "VERSION NOT SET";
         
      if (resourceURL != null) {
         try {
            final InputStream resourceStream = resourceURL.openStream();
            
            actualVersion = new String(RawInputStreamReader.instance().readFully(resourceStream));
            
            resourceStream.close();
         } catch (IOException ioe) {
            LOGGER.error("Unable to read build version info. This may be a packaging error and should be reported upstream.", ioe);
         }
      }
      
      this.versionString = actualVersion;
   }

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      if (GET_METHOD.equalsIgnoreCase(req.getMethod())) {
         resp.setStatus(200);
         resp.getOutputStream().write(versionString.getBytes());
      } else {
         resp.setStatus(405);
      }
   }
}
