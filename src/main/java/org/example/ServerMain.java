package org.example;

import lombok.SneakyThrows;
import org.example.annotation.AnnotationServlet;
import org.example.controller.SearchController;
import org.example.controller.SiteController;
import org.example.servlet.*;

import java.util.List;

public class ServerMain {

    private static final int PORT = 12345;

    @SneakyThrows
    public static void main(String[] args) {
        List<Object> controllers = List.of(new SiteController(), new SearchController());
        AnnotationServlet servlet = new AnnotationServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(servlet);
        servletManager.add("/favicon.ico", new DiscardServlet());
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}