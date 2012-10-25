package org.atomnuke.collectd.servlet;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.IOException;
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
public class CollectdSinkServlet extends HttpServlet {

   private static final Logger LOG = LoggerFactory.getLogger(CollectdSinkServlet.class);

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      LOG.info("Method: " + req.getMethod());
      LOG.info("Content: " + new String(RawInputStreamReader.instance().readFully(req.getInputStream())));
   }
}
