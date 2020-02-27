package org.me.calculator.client;

import java.io.*;
import javax.annotation.Resource;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author fabio_vargas
 */
@WebServlet(name = "ClientServlet", urlPatterns = {"/ClientServlet"})
public class ClientServlet extends HttpServlet {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/CalculatorApp/CalculatorWSService?wsdl")
    public CalculatorWSService service;

    @Resource
    protected WebServiceContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<h2>Servlet ClientServlet at " + request.getContextPath() + "</h2>");

            org.me.calculator.client.CalculatorWS port = service.getCalculatorWSPort();

            int i = Integer.parseInt(request.getParameter("value1"));
            int j = Integer.parseInt(request.getParameter("value2"));

            int result = port.add(i, j);

            out.println("<br/>");
            out.println("Result:");
            out.println("" + i + " + " + j + " = " + result);
            ((Closeable) port).close();

        } finally {
            out.close();
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

}
