package org.example;

import lombok.SneakyThrows;
import org.example.servlet.*;

public class ServerMain {

    private static final int PORT = 12345;

    @SneakyThrows
    public static void main(String[] args) {
        ServletManager servletManager = new ServletManager();
        servletManager.add("/", new HomeServlet());
        servletManager.add("/site1", new Site1Servlet());
        servletManager.add("/site2", new Site2Servlet());
        servletManager.add("/search", new SearchServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
