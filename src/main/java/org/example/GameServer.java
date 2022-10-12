package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer implements Serializable, Runnable {

    @Serial
    private static final long serialVersionUID = 1L;
    public boolean isAcceptingConnections;
    private int turnsMade;
    private int maxTurns;
    private int currentPlayer = 0;

    Server[] playerServer = new Server[3];
    Player[] players = new Player[3];

    ServerSocket ss;


    int numPlayers;
    Boolean isRunning = true;

    public static void main(String[] args) throws Exception {
        GameServer sr = new GameServer();

        sr.acceptConnections();
        sr.gameLoop();
    }

    @Override
    public void run() {
        try {
            acceptConnections();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        while(isRunning){

        }
    }

    public GameServer() {
        System.out.println("Starting game server");
        numPlayers = 0;
        turnsMade = 0;
        maxTurns = 13;
        // initialize the players list with new players
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(" ");
        }

        try {
            ss = new ServerSocket(Config.GAME_SERVER_PORT_NUMBER);
        } catch (IOException ex) {
            System.out.println("Server Failed to open");
        }

    }

    /**
     * Resets the server
     *
     */
    synchronized public String hardReset() throws IOException {
        numPlayers = 0;
        turnsMade = 0;
        maxTurns = 13;
        currentPlayer = 0;
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(" ");
            playerServer[i].terminate();
            playerServer[i] = null;
        }
        return "server reset";
    }

    synchronized public String [] getPlayerOrder(){
        String [] playerOrder = new String [Config.NUM_OF_PLAYERS];
        for (int i = 0; i < Config.NUM_OF_PLAYERS; i++){
            playerOrder[i] = players[i].name;
        }
        return playerOrder;
    }

    /*
     * -----------Networking stuff ----------
     *
     */
    synchronized public void acceptConnections() throws ClassNotFoundException {
        try {
            System.out.println("Waiting for players...");
            while (numPlayers < 3) {
                isAcceptingConnections = true;
                Socket s = ss.accept();
                numPlayers++;

                Server server = new Server(s, numPlayers);

                // send the player number
                server.dOut.writeInt(server.playerId);
                server.dOut.flush();

                // get the player name
                Player in = (Player) server.dIn.readObject();
                System.out.println("Player " + server.playerId + " ~ " + in.name + " ~ has joined");
                // add the player to the player list
                players[server.playerId - 1] = in;
                playerServer[numPlayers - 1] = server;
            }
            System.out.println("Three players have joined the game");

            // start the server threads
            for (int i = 0; i < playerServer.length; i++) {
                Thread t = new Thread(playerServer[i]);
                t.start();
            }
            // start their threads
        } catch (IOException ex) {
            System.out.println("Could not connect 3 players");
        }
        isAcceptingConnections = false;
    }

    synchronized public void gameLoop() {
        System.out.println("Inside of gameloop");


    }


    public class Server implements Runnable {
        private Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        private int playerId;
        private Boolean isRunning = true;

        public Server(Socket s, int playerid) {
            socket = s;
            playerId = playerid;
            try {
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Server Connection failed");
            }
        }

        /*
         * run function for threads --> main body of the thread will start here
         */
        public void run() {
            try {
                while (isRunning) {
                }

            } catch (Exception ex) {
                {
                    System.out.println("Run failed");
                    ex.printStackTrace();
                }
            }
        }


        void terminate() throws IOException {
            socket.close();
            isRunning = false;
        }

    }

}
