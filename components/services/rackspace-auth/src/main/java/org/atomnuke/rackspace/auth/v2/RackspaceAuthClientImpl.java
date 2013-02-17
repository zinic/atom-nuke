package org.atomnuke.rackspace.auth.v2;

import org.atomnuke.auth.AuthServiceException;
import com.rackspace.docs.identity.api.ext.rax_kskey.v1.ApiKeyCredentials;
import com.rackspace.docs.identity.api.ext.rax_kskey.v1.ObjectFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.atomnuke.lifecycle.InitializationException;
import org.openstack.docs.identity.api.v2.AuthenticateResponse;
import org.openstack.docs.identity.api.v2.AuthenticationRequest;
import org.openstack.docs.identity.api.v2.Token;

/**
 *
 * @author zinic
 */
public class RackspaceAuthClientImpl implements RackspaceAuthClient {

   private static final org.openstack.docs.identity.api.v2.ObjectFactory REQUEST_FACTORY = new org.openstack.docs.identity.api.v2.ObjectFactory();
   private static final ObjectFactory CREDENTIALS_FACTORY = new ObjectFactory();
   
   private final Unmarshaller jaxUnmarshaller;
   private final Marshaller jaxbMarshaller;
   private final JAXBContext jaxbContext;
   private final HttpClient httpClient;
   private final URI authApiEndpoint;

   public RackspaceAuthClientImpl(HttpClient httpClient, URI authApiEndpoint) throws InitializationException {
      this.httpClient = httpClient;
      this.authApiEndpoint = authApiEndpoint;

      try {
         jaxbContext = JAXBContext.newInstance(org.openstack.docs.identity.api.v2.ObjectFactory.class,
                 com.rackspace.docs.identity.api.ext.rax_auth.v1.ObjectFactory.class);

         jaxUnmarshaller = jaxbContext.createUnmarshaller();
         jaxbMarshaller = jaxbContext.createMarshaller();
      } catch (JAXBException jaxbe) {
         throw new InitializationException(jaxbe);
      }
   }

   @Override
   public Token authenticate(ApiKeyCredentials apiKeyCredentials) throws AuthServiceException {
      final AuthenticationRequest requestObject = new AuthenticationRequest();
      requestObject.setCredential(CREDENTIALS_FACTORY.createApiKeyCredentials(apiKeyCredentials));

      final HttpPost authenticationRequest = new HttpPost(authApiEndpoint);
      authenticationRequest.setHeader("Content-Type", "application/xml");
      authenticationRequest.setHeader("Accept", "application/xml");

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
         jaxbMarshaller.marshal(REQUEST_FACTORY.createAuth(requestObject), baos);
      } catch (JAXBException jaxbe) {
         throw new AuthServiceException(jaxbe);
      }

      authenticationRequest.setEntity(new ByteArrayEntity(baos.toByteArray()));
      InputStream responseInputStream = null;

      try {
         final HttpResponse httpResponse = httpClient.execute(authenticationRequest);
         final int statusCode = httpResponse.getStatusLine().getStatusCode();
         
         if (statusCode == 200) {
            responseInputStream = httpResponse.getEntity().getContent();
            
            final Object jaxbObject = jaxUnmarshaller.unmarshal(responseInputStream);
            final AuthenticateResponse authenticateResponse = 
                    (AuthenticateResponse) (jaxbObject instanceof JAXBElement ? ((JAXBElement) jaxbObject).getValue() : jaxbObject);

            responseInputStream.close();

            return authenticateResponse.getToken();
         }
         
         throw new AuthServiceException("Auth request failed with response code: " + statusCode);
      } catch (IOException ioe) {
         throw new AuthServiceException(ioe);
      } catch (JAXBException jaxbe) {
         throw new AuthServiceException(jaxbe);
      }
   }
}
