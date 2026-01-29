package org.example.controller;

import org.example.HttpRequest;
import org.example.HttpResponse;

public class SiteController {
    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }
    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
