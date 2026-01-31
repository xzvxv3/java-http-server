package org.example.annotation;

import lombok.RequiredArgsConstructor;
import org.example.core.HttpRequest;
import org.example.core.HttpResponse;
import org.example.exception.PageNotFoundException;
import org.example.servlet.HttpServlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnnotationServlet implements HttpServlet {

    private final Map<String, ControllerMethod> pathMap;

    public AnnotationServlet(List<Object> controllers) {
        this.pathMap = new HashMap<>();
        initializePathMap(controllers);
    }

    private void initializePathMap(List<Object> controllers) {
        for (Object controller : controllers) {
            for(Method method : controller.getClass().getDeclaredMethods()) {
                if(method.isAnnotationPresent(Mapping.class)) {
                    String path = method.getAnnotation(Mapping.class).value();

                    if(pathMap.containsKey(path)) {
                        ControllerMethod controllerMethod = pathMap.get(path);
                        throw new IllegalArgumentException("경로 중복 등록, path=" + path + ", method=" + method + ", 이미 등록된 메서드=" + controllerMethod.method);
                    }

                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        ControllerMethod controllerMethod = pathMap.get(path);

        if (controllerMethod == null) {
            throw new PageNotFoundException("request=" + path);
        }

        controllerMethod.invoke(request, response);
    }

    @RequiredArgsConstructor
    private static class ControllerMethod {
        private final Object controller;
        private final Method method;

        public void invoke(HttpRequest request, HttpResponse response) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for(int i = 0; i < parameterTypes.length; i++) {
                if(parameterTypes[i] == HttpRequest.class) {
                    args[i] = request;
                }
                else if(parameterTypes[i] == HttpResponse.class) {
                    args[i] = response;
                }
                else {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
                }
            }

            try {
                method.invoke(controller, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
