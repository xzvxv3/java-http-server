package org.example;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Slf4j
public class HttpRequestHandler implements Runnable {
    private final Socket socket;

    @Override
    public void run() {
        process();
    }

    // 서버 로직 (클라이언트의 요청 수신 -> 요청 처리 -> 클라이언트에게 응답 송신)
    @SneakyThrows
    private void process() {
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

            Thread.sleep(1500); // 서버의 요청 처리 시간

            if(clientRequest.startsWith("GET /site1")) {
                site1(writer);
            } else if(clientRequest.startsWith("GET /site2")) {
                site2(writer);
            } else if(clientRequest.startsWith("GET /search")) {
                search(writer, clientRequest);
            } else if(clientRequest.startsWith("GET / ")) {
                home(writer);
            } else {
                notFound(writer);
            }

            log.info("HTTP 응답 전달 완료");
        }
    }

    private static void home(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>home</h1>");
        writer.println("<ul>");
        writer.println("<li><a href='/site1'>site1</a></li>");
        writer.println("<li><a href='/site2'>site2</a></li>");
        writer.println("<li><a href='/search?q=hello'>검색</a></li>");
        writer.println("</ul>");
        writer.flush();
    }

    private static void site1(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>site1</h1>");
        writer.flush();
    }

    private static void site2(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>site2</h1>");
        writer.flush();
    }

    private static void notFound(PrintWriter writer) {
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>404 페이지를 찾을 수 없습니다.</h1>");
        writer.flush();
    }

    private static void search(PrintWriter writer, String requestString) {
        int startIndex = requestString.indexOf("q=");
        int endIndex = requestString.indexOf(" ", startIndex+2);
        String query = requestString.substring(startIndex+2, endIndex);
        String decode = URLDecoder.decode(query, UTF_8);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>Search</h1>");
        writer.println("<ul>");
        writer.println("<li>query: " + query + "</li>");
        writer.println("<li>decode: " + decode + "</li>");
        writer.println("</ul>");
        writer.flush();
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
}
