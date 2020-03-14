package org.me.calculatorclientapp;

import org.me.calculatorclientwso2.dto.AccessToken;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.me.calculatorclientapp.util.Base64Encoded;

/**
 *
 * @author fabio_vargas
 */
public class TokenService {

    @WebMethod(operationName = "getToken")
    public AccessToken getToken(@WebParam(name = "userName") String userName, @WebParam(name = "Password") String password) throws Exception {

        AccessToken acessToken = null;
        Response response = null;
        Client cliente = ClientBuilder.newClient();
        String encodeCredentials = Base64Encoded.encodeCredentials(userName, password);
        response = cliente.target("https://localhost:8243/token")
                .queryParam("grant_type", "client_credentials").request()
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodeCredentials)
                .buildPost(null)
                .invoke();

        if (response.getStatus() == 200) {
            acessToken = response.readEntity(AccessToken.class);
            System.out.println(acessToken.getAccessToken());
        }
        return acessToken;
    }

}
