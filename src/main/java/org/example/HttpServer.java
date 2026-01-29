package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Slf4j
public class HttpServer {

    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private final int port;
    private final ServletManager servletManager;

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        log.info("Server starting on port: {}", port);

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                es.submit(new HttpRequestHandler(socket, servletManager));
            } catch (Exception e) {
                log.error("클라이언트 요청 처리 중 에러 발생 : {}", e.getMessage());
            }
        }
    }
}
