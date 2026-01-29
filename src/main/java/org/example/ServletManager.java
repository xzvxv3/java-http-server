package org.example;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.PageNotFoundException;
import org.example.servlet.HttpServlet;
import org.example.servlet.InternalErrorServlet;
import org.example.servlet.NotFoundServlet;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Slf4j
public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;
    private HttpServlet notFoundErrorServlet = new NotFoundServlet();
    private HttpServlet internalErrorServlet = new InternalErrorServlet();

    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }

    public void setNotFoundErrorServlet(HttpServlet notFoundErrorServlet) {
        this.notFoundErrorServlet = notFoundErrorServlet;
    }

    public void setInternalErrorServlet(HttpServlet internalErrorServlet) {
        this.internalErrorServlet = internalErrorServlet;
    }

    @SneakyThrows
    public void execute(HttpRequest request, HttpResponse response) {
        try {
            HttpServlet servlet = servletMap.getOrDefault(request.getPath(), defaultServlet);
            servlet.service(request, response);
        } catch (PageNotFoundException e) {
            log.error(e.getMessage());
            notFoundErrorServlet.service(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            internalErrorServlet.service(request, response);
        }
    }
}
