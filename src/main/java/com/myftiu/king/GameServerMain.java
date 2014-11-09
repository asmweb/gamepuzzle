package com.myftiu.king;

import com.myftiu.king.server.GameServer;
import com.myftiu.king.server.GameServerImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author by ali myftiu.
 */
public class GameServerMain {



	private final static Logger LOGGER = Logger.getLogger(GameServerMain.class.getName());
    private static final GameServer gameServer = new GameServerImpl();



	public static void main(String[] args) throws IOException {
        addServerShutdownHook();
        LOGGER.log(Level.INFO, "Server is going to start");
        gameServer.startServer();
    }

    private static void addServerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                LOGGER.log(Level.INFO, "Stopping server");
                gameServer.stopServer();
            }
        });
    }




}
