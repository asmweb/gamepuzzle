package com.myftiu.king;

import com.myftiu.king.filter.CustomFilter;
import com.myftiu.king.service.CustomHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by myftiu on 04/11/14.
 */
public class GameServerMain {


    private final static int SERVER_PORT = 8009;
	private final static Logger LOGGER = Logger.getLogger(GameServerMain.class.getName());


	public static void main(String[] args) throws IOException {


        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);


        HttpContext context = server.createContext("/", new CustomHandler());

        // Custom filtering the user calls
        context.getFilters().add(new CustomFilter());

        // A thread pool is created
        server.setExecutor(Executors.newCachedThreadPool());


        server.start();

		LOGGER.log(Level.INFO, "Server started!");

    }


}
