package org.example;

import lombok.SneakyThrows;

public class ServerMain {

    private static final int PORT = 12345;

    @SneakyThrows
    public static void main(String[] args) {
        HttpServer server = new HttpServer(PORT);
        server.start();
    }
}
