package org.example.servlet;

import org.example.HttpRequest;
import org.example.HttpResponse;

import java.io.IOException;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
