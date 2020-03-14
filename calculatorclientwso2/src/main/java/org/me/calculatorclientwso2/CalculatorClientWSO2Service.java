package org.me.calculatorclientwso2;

import org.me.calculatorclientwso2.dto.AccessToken;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
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
        URL url = new URL("http://lt9c1dyr2:7001/App/CalculatorWSService?wsdl");
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

    public static CalculatorWS applySecurityAccess(Service service) throws Exception {

        String userName = "orjivDl4s_W9ircyB9wCUIJ9aXEa";
        String passWord = "r0WkOBNhmg4T4L4nYuwgeXaNu1Ua";
        TokenService token = new TokenService();
        AccessToken acess = token.getToken(userName, passWord);
        String accessToken = acess.getAccessToken();
        CalculatorWS port = service.getPort(CalculatorWS.class);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://lt9c1dyr2:8280/CalculatorWSService/1.0");
        return port;
    }

}
