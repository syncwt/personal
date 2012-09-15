package com.beust.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) {
        AsyncContext context = req.getAsyncContext();
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print("It works!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
