package org.example.servlet;

import org.example.HttpRequest;
import org.example.HttpResponse;

public class Site1Servlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }
}
