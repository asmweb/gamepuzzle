package com.myftiu.king.server;

import com.myftiu.king.ServerConfig;
import com.myftiu.king.filter.CustomFilter;
import com.myftiu.king.service.CustomHandler;
import com.myftiu.king.service.ScoreService;
import com.myftiu.king.service.SessionService;
import com.myftiu.king.service.TimeDefinition;
import com.myftiu.king.service.impl.ScoreServiceImpl;
import com.myftiu.king.service.impl.SessionServiceImpl;
import com.myftiu.king.service.impl.TimeDefinitionImpl;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author by ali myftiu.
 */
public class GameServerImpl implements GameServer {

    private HttpServer server;
    private final static Logger LOGGER = Logger.getLogger(GameServerImpl.class.getName());
    private SessionService sessionService;
    private ScoreService scoreService;
	private TimeDefinition timeDefinition;


    public void startServer() throws IOException {

		timeDefinition = new TimeDefinitionImpl();
        sessionService = new SessionServiceImpl(timeDefinition);
        scoreService = new ScoreServiceImpl();
        server = HttpServer.create(new InetSocketAddress(ServerConfig.SERVER_PORT), 0);


        HttpContext context = server.createContext("/", new CustomHandler(sessionService, scoreService));

        // Custom filtering the user calls
        context.getFilters().add(new CustomFilter());

        // A thread pool is created
        server.setExecutor(Executors.newCachedThreadPool());


        server.start();

        LOGGER.log(Level.INFO, "Server started!");
    }


    public void stopServer() {
        if(server != null) server.stop(0);
    }
}
