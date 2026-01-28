package org.example;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;

@Slf4j
@Getter
@ToString
public class HttpRequest {
    private String method; // HTTP 요청 메서드, ex) GET, POST ...
    private String path; // 요청 경로

    // ?q=hello&key2=value2 (key = q, value = hello 형식)
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) {
        parseRequestLine(reader);
        parseHeaders(reader);
    }

    // GET /search?q=hello HTTP/1.1
    @SneakyThrows
    private void parseRequestLine(BufferedReader reader) {
        String requestLine = reader.readLine(); // bufferedReader -> 한 줄씩 처리
        if(requestLine == null) {
            throw new IOException("EOF: No request line received");
        }

        String[] parts = requestLine.split(" ");
        if(parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        method = parts[0];

        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        if(pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }
    }

//     q=hello
//     key1=value1&key2=value2
//     키1=값1 -> %키1 = %값1
//     q=
    private void parseQueryParameters(String queryString) {
        for(String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }

//        Host: localhost:12345
//        Connection: keep-alive
//        Cache-Control: max-age=0
//        (Empty)
    @SneakyThrows
    private void parseHeaders(BufferedReader reader) {
        String line;
        while(!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(":");
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
    }

    public String getParameter(String name) {
        return queryParameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}

