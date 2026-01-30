package org.example.controller;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.annotation.Mapping;

public class SearchController {
    @Mapping("/search")
    public void search(HttpRequest request, HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}