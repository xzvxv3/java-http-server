package org.example;

import lombok.SneakyThrows;
import org.example.controller.SearchController;
import org.example.controller.SiteController;
import org.example.servlet.*;

import java.util.List;

public class ServerMain {

    private static final int PORT = 12345;

    @SneakyThrows
    public static void main(String[] args) {
        List<Object> controllers = List.of(new SiteController(), new SearchController());
        ReflectionServlet reflectionServlet = new ReflectionServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
