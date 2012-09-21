package com.beust.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private int index = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final AsyncContext aCtx = request.startAsync(request, response);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                while(true) {
                    PrintWriter out = aCtx.getResponse().getWriter();
                    String message = "Time: " + System.currentTimeMillis();

                    out.print(message);
                    out.flush();
                    Thread.sleep(1000);
                }
            }
            
        });

        PrintWriter out;
        try {
            out = response.getWriter();
            out.print("Index:" + index++);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
