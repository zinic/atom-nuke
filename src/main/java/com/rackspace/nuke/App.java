package com.rackspace.nuke;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Hello world!
 *
 */
public class App {

   public static void main(String[] args) throws IOException {
      final HttpClient httpClient = new DefaultHttpClient();
      final HttpGet httpGet = new HttpGet("https://atom.staging.ord1.us.ci.rackspace.net/nova/events");

      final HttpResponse response = httpClient.execute(httpGet);

      final HttpEntity entity = response.getEntity();
      final InputStream inputStream = entity.getContent();
      
      final String string = new String(RawInputStreamReader.instance().readFully(inputStream));
      
      System.out.println(string);
   }
}
