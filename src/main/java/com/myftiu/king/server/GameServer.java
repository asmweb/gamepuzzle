package com.myftiu.king.server;

import com.myftiu.king.filter.CustomFilter;
import com.myftiu.king.service.CustomHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by myftiu on 04/11/14.
 */
public class GameServer {


    private static int port = 8009;

    public static void main(String[] args) throws IOException {


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);


        HttpContext context = server.createContext("/", new CustomHandler());

        // Custom filtering the user calls
        context.getFilters().add(new CustomFilter());

        // A thread pool is created
        server.setExecutor(Executors.newCachedThreadPool());


        server.start();

        System.out.println("The server is started!");
    }


}
