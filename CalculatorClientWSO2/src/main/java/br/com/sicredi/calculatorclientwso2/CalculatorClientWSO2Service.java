package br.com.sicredi.calculatorclientwso2;

import br.com.sicredi.dto.AccessToken;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.codec.binary.Base64;
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

    public static CalculatorWS applySecurityAccess(Service service) throws Exception{
        
        String userName = "orjivDl4s_W9ircyB9wCUIJ9aXEa";
        String passWord = "r0WkOBNhmg4T4L4nYuwgeXaNu1Ua";
        Token token = new Token();
        AccessToken acess = token.getToken(userName, passWord);
        String accessToken = acess.getAccessToken();
        CalculatorWS port = service.getPort(CalculatorWS.class);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://lt9c1dyr2:8280/calculator/1.0");
        return port;
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

}
