package org.example;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Slf4j
public class HttpRequestHandler implements Runnable {
    private final Socket socket;

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 서버 로직 (클라이언트의 요청 수신 -> 요청 처리 -> 클라이언트에게 응답 송신)
    @SneakyThrows
    private void process() {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8)) {

            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(writer);

            // Favicon 요청은 생략
            if(request.getPath().equals("/favicon.ico")) {
                log.info("favicon 요청");
                return;
            }

            log.info("클라이언트의 HTTP 요청 정보 출력\n{}", request);

            log.info("HTTP 응답 생성중...");

            Thread.sleep(1500); // 서버의 요청 처리 시간

            if(request.getPath().equals("/site1")) {
                site1(response);
            } else if(request.getPath().equals("/site2")) {
                site2(response);
            } else if(request.getPath().equals("/search")) {
                search(request, response);
            } else if(request.getPath().equals("/")) {
                home(response);
            } else {
                notFound(response);
            }

            response.flush();

            log.info("HTTP 응답 전달 완료");
        }
    }

    private static void home(HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    private static void site1(HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    private static void site2(HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }

    private static void search(HttpRequest request, HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }

    private static void notFound(HttpResponse response) {
        response.setStatusCode(404);
        response.writeBody("<h1>404 페이지를 찾을 수 없습니다.</h1>");
    }
}
