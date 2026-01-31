package org.example;

import lombok.SneakyThrows;
import org.example.annotation.AnnotationServlet;
import org.example.controller.MemberController;
import org.example.core.HttpServer;
import org.example.member.FileMemberRepository;
import org.example.member.MemberRepository;
import org.example.servlet.*;

import java.util.List;

public class ServerMain02 {

    private static final int PORT = 12345;

    @SneakyThrows
    public static void main(String[] args) {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberController memberController = new MemberController(memberRepository);

        HttpServlet servlet = new AnnotationServlet(List.of(memberController));
        ServletManager servletManager = new ServletManager();
        servletManager.add("/favicon.ico", new DiscardServlet());
        servletManager.setDefaultServlet(servlet);

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}