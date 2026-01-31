package org.example.servlet;

import org.example.core.HttpRequest;
import org.example.core.HttpResponse;

public class InternalErrorServlet implements HttpServlet{
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setStatusCode(500);
        response.writeBody("<h1>Internal Error</h1>");
    }
}
