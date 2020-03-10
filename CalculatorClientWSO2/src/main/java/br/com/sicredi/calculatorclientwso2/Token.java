package br.com.sicredi.calculatorclientwso2;

import br.com.sicredi.dto.AccessToken;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author fabio_vargas
 */
public class Token {

    final static String TRUST_STORE = "C:\\Temp\\keystore.jks";
    final static String CLIENT_KEY_STORE = "C:\\Temp\\keystore.jks";
    final static String CLIENT_KEY_STORE_PASS = "changeit";
    final static String CLIENT_KEY_ALIAS = "localhost";

    @WebMethod(operationName = "getToken")
    public AccessToken getToken(@WebParam(name = "userName") String userName, @WebParam(name = "Password") String password) throws Exception {

        AccessToken acessToken = null;
        Response response = null;
        setupSSL();
        Client cliente = ClientBuilder.newClient();
        String encodeCredentials = encodeCredentials(userName, password);
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

    public static String encodeCredentials(String username, String password) {
        String cred = username + ":" + password;
        String encodedValue = null;
        byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
        encodedValue = new String(encodedBytes);
        System.out.println("encodedBytes " + new String(encodedBytes));
        byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
        System.out.println("decodedBytes " + new String(decodedBytes));
        return encodedValue;
    }

    public static void setupSSL() {
        System.setProperty("javax.net.ssl.trustStore", TRUST_STORE);
        System.out.println("javax.net.ssl.trustStore: " + System.getProperty("javax.net.ssl.trustStore"));
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost") || hostname.equals("172.22.82.40")) {
                    return true;
                }
                return false;
            }
        });
    }

}
