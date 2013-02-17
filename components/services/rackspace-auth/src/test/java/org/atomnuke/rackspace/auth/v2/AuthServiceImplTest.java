package org.atomnuke.rackspace.auth.v2;

import com.rackspace.docs.identity.api.ext.rax_kskey.v1.ApiKeyCredentials;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.openstack.docs.identity.api.v2.Token;
import static org.mockito.Mockito.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class AuthServiceImplTest {

   public static class WhenAuthenticating {

      @Test
      public void shouldAuthenticate() throws Exception {
         final HttpResponse mockResponse = mock(HttpResponse.class);
         final StatusLine mockStatus = mock(StatusLine.class);
         final HttpEntity mockEntity = mock(HttpEntity.class);
         
         when(mockResponse.getEntity()).thenReturn(mockEntity);
         when(mockResponse.getStatusLine()).thenReturn(mockStatus);
         when(mockStatus.getStatusCode()).thenReturn(Integer.valueOf(200));
         when(mockEntity.getContent()).thenReturn(AuthServiceImplTest.class.getResourceAsStream("/test_auth_response.xml"));
         
         final HttpClient mockHttpClient = mock(HttpClient.class);
         
         when(mockHttpClient.execute(any(HttpPost.class))).thenReturn(mockResponse);
         
         final RackspaceAuthClientImpl authSvc = new RackspaceAuthClientImpl(mockHttpClient, URI.create("https://identity.api.rackspacecloud.com/v2.0/tokens"));
         final ApiKeyCredentials credentials = new ApiKeyCredentials();
         credentials.setUsername("user");
         credentials.setApiKey("gjaoeijgoaeijgioaejgioaej");

         final Token token = authSvc.authenticate(credentials);

         System.out.println("Got token: " + token.getId());
      }
   }
}
