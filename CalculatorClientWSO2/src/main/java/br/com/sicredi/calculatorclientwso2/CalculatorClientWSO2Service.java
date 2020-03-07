package br.com.sicredi.calculatorclientwso2;

import br.com.sicredi.oauth.TrustCertificate;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.me.calculator.CalculatorWS;
import org.me.calculator.CalculatorWSService;

/**
 *
 * @author fabio_vargas
 */
@WebService(serviceName = "CalculatorClientWSO2Service")
public class CalculatorClientWSO2Service {

    @WebMethod(operationName = "add")
    public int add(@WebParam(name = "i") int i, @WebParam(name = "j") int j) throws Exception {

        URL url = new URL("http://lt9c1dyr2:8080/CalculatorApp/CalculatorWSService?wsdl");
        QName qname = new QName("http://calculator.me.org/", "CalculatorWSService");
        Service service = Service.create(url, qname);
        CalculatorWS calculator = service.getPort(CalculatorWS.class);

        return calculator.add(i, j);
    }

    @WebMethod(operationName = "addWSO2")
    public int addWSO2(@WebParam(name = "i") int i, @WebParam(name = "j") int j) throws Exception {

        Service service = new CalculatorWSService();
        CalculatorWS calculatorService = applySecurityAccess(service);
        return calculatorService.add(i, j);
    }

    public static CalculatorWS applySecurityAccess(Service service) {
        String accessToken = "c3a050b6-a49f-3a8f-bfb0-23daa65f7bda";
        CalculatorWS port = service.getPort(CalculatorWS.class);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://10.0.75.1:8280/calculator/1.0");
        return port;
    }

    @WebMethod(operationName = "getToken")
    public Response getToken(@WebParam(name = "userName") String userName, @WebParam(name = "Password") String password) throws Exception {

        //Confiar no certificado utilizado na requisicao https
        TrustCertificate.allowAllCerts();
        //httpGETCollectionExample();

        Client cliente = ClientBuilder.newClient();
        Response response = null;
        String encodeCredentials = encodeCredentials(userName, password);
        try {
            response = cliente.target("https://lt9c1dyr2:8243/token")
                    .queryParam("grant_type", "client_credentials").request()
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + encodeCredentials)
                    .buildPost(null)
                    .invoke();
        } catch (Exception e) {
            System.out.println(e);
            return response;
        }

        return response;
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

    private static void httpGETCollectionExample() {
        ClientConfig clientConfig = configureClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("orjivDl4s_W9ircyB9wCUIJ9aXEa", "r0WkOBNhmg4T4L4nYuwgeXaNu1Ua");
        clientConfig.register(feature);

        //clientConfig.register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target("https://lt9c1dyr2:9443/oauth2/token?grant_type=client_credentials");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_FORM_URLENCODED);
        Response response = invocationBuilder.post(null);

        System.out.println(response.getStatus());

        if (response.getStatus() == 200) {
//        Employees employees = response.readEntity(Employees.class);
//        List<Employee> listOfEmployees = employees.getEmployeeList();
//        System.out.println(Arrays.toString( listOfEmployees.toArray(new Employee[listOfEmployees.size()]) ));
        }

    }

    public static ClientConfig configureClient() {
        TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());
        } catch (java.security.GeneralSecurityException ex) {
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        ClientConfig config = new ClientConfig();
        try {
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                    new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            },
                    ctx
            ));
        } catch (Exception e) {
        }

        return config;
    }

}
