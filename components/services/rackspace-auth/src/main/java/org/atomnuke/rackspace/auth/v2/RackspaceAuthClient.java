package org.atomnuke.rackspace.auth.v2;

import org.atomnuke.auth.AuthServiceException;
import com.rackspace.docs.identity.api.ext.rax_kskey.v1.ApiKeyCredentials;
import org.openstack.docs.identity.api.v2.Token;

/**
 *
 * @author zinic
 */
public interface RackspaceAuthClient {

   Token authenticate(ApiKeyCredentials apiKeyCredentials) throws AuthServiceException;
}
