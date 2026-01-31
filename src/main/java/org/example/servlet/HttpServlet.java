package org.example.servlet;

import org.example.core.HttpRequest;
import org.example.core.HttpResponse;

import java.io.IOException;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
