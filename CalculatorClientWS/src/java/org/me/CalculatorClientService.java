package org.me;

import java.net.URL;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.me.calculator.CalculatorWS;

/**
 *
 * @author fabio_vargas
 */
@WebService(serviceName = "CalculatorClientService")
public class CalculatorClientService {

    @WebMethod(operationName = "add")
    public int add(@WebParam(name = "i") int i, @WebParam(name = "j") int j) throws Exception {

        URL url = new URL("http://10.0.75.1:8080/CalculatorApp/CalculatorWSService?wsdl");
        QName qname = new QName("http://calculator.me.org/", "CalculatorWSService");
        Service service = Service.create(url, qname);
        CalculatorWS calculator = service.getPort(CalculatorWS.class);
        return calculator.add(i, j);

    }
}
