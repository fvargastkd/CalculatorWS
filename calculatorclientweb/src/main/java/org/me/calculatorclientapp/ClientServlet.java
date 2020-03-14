package org.me.calculatorclientapp;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.handler.MessageContext;
import org.me.calculator.CalculatorWS;
import org.me.calculator.CalculatorWSService;
import org.me.calculatorclientwso2.dto.AccessToken;

/**
 *
 * @author fabio_vargas
 */
@WebServlet(name = "ClientServlet", urlPatterns = {"/ClientServlet"})
public class ClientServlet extends HttpServlet {

    @WebServiceRef(wsdlLocation = "http://lt9c1dyr2:7001/App/CalculatorWSService?WSDL")
    public CalculatorWSService service;

    @Resource
    protected WebServiceContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<h2>Servlet ClientServlet at " + request.getContextPath() + "</h2>");
            Service servicews = new CalculatorWSService();
            CalculatorWS port = applySecurityAccess(servicews);

            int i = Integer.parseInt(request.getParameter("value1"));
            int j = Integer.parseInt(request.getParameter("value2"));

            int result = port.add(i, j);

            out.println("<br/>");
            out.println("Result:");
            out.println("" + i + " + " + j + " = " + result);
            ((Closeable) port).close();

            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public static CalculatorWS applySecurityAccess(Service service) throws Exception {

        String userName = "orjivDl4s_W9ircyB9wCUIJ9aXEa";
        String passWord = "r0WkOBNhmg4T4L4nYuwgeXaNu1Ua";
        TokenService token = new TokenService();
        AccessToken acess = token.getToken(userName, passWord);
        String accessToken = acess.getAccessToken();
        CalculatorWS port = service.getPort(CalculatorWS.class);
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://lt9c1dyr2:8280/CalculatorWSService/1.0");
        return port;
    }

}
