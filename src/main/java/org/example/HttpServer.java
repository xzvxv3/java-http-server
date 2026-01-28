package org.example;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.*;

@Slf4j
@RequiredArgsConstructor
public class HttpServer {

    private final int port;

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        log.info("Server starting on port: {}", port);

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                process(socket);
            } catch (Exception e) {
                log.error("클라이언트 요청 처리 중 에러 발생 : {}", e.getMessage());
            }
        }
    }

    @SneakyThrows
    private void process(Socket socket) {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8)) {

            // 클라이언트가 보낸 HTTP 헤더를 읽어옴
            String clientRequest = requestToString(reader);

            // Favicon 요청은 생략
            if(clientRequest.contains("/favicon.ico")) {
                log.info("favicon 요청");
                return;
            }

            log.info("클라이언트의 HTTP 요청 정보 출력\n{}", clientRequest);

            log.info("HTTP 응답 생성중...");
            Thread.sleep(3000); // 서버의 요청 처리 시간

            // 클라이언트에게 응답 처리
            responseToClient(writer);
            log.info("HTTP 응답 전달 완료");
        }
    }

    // 클라이언트의 요청을 문자열로 변환해서 반환
    @SneakyThrows
    private String requestToString(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            if(line.isEmpty()) break;
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    // 클라이언트에게 응답 처리
    private void responseToClient(PrintWriter writer) {
        String body = "<h1>Hello World</h1>";
        int length = body.getBytes(UTF_8).length;

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Content-Length: ").append(length).append("\r\n");
        sb.append("\r\n"); // header, body 구분 라인
        sb.append(body);

        log.info("HTTP 응답 정보 출력\n{}", sb);

        writer.println(sb);
        writer.flush();
    }
}
