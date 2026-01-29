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
    private final ServletManager servletManager;

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

            log.info("HTTP 요청: " + request);
            Thread.sleep(1500); // 서버의 요청 처리 시간

            servletManager.execute(request, response);
            response.flush();
            log.info("HTTP 응답 완료");
        }
    }
}
