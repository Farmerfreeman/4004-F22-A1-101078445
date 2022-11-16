package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class GameServer implements Serializable, Runnable {

    @Serial
    private static final long serialVersionUID = 1L;
    public boolean isAcceptingConnections;
    public States state;

    AtomicInteger playerTurn =new AtomicInteger(0);

    private int currentPlayer = 0;

    private int countdown = 3;

    boolean test = false;

    AtomicBoolean gameOver = new AtomicBoolean();

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
        if (test){
            try {
                acceptConnections(true);
                gameLoop(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            while(isRunning){

            }
        }
        else {
            try {
                acceptConnections();
                gameLoop();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            while (isRunning) {

            }
        }
    }



    public GameServer() {
        System.out.println("Starting game server");
        numPlayers = 0;

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

        currentPlayer = 0;
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(" ");
            playerServer[i].terminate();
            playerServer[i] = null;
        }
        return "server reset";
    }

    public void closeAll() throws IOException{
        for (Server p : playerServer){
            p.terminate();
        }
        ss.close();
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

    synchronized public void acceptConnections(boolean test) throws ClassNotFoundException {
        try {
            System.out.println("Waiting for players...");
            while (numPlayers < 1) {
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

        int highscore = 0;
        try {
            playerServer[0].sendPlayers(players);
            playerServer[1].sendPlayers(players);
            playerServer[2].sendPlayers(players);
            state = States.PLAYERTURN_1;
            playerTurn.set(1);
            while (state != States.GAMEOVER){


                playerServer[0].sendState(state);
                playerServer[1].sendState(state);
                playerServer[2].sendState(state);

                States pState;
                pState = playerServer[currentPlayer].receiveState();

                if (pState == States.SKULL_ISLAND){
                    System.out.println(players[currentPlayer].name + " has reached skull island!");
                    playerServer[currentPlayer].receiveState();
                    int skullScore = playerServer[currentPlayer].recieveScore();
                    System.out.println("All other players will suffer a deduction of " + skullScore);

                    int[] scores = new int[3];



                    for (int i = 0; i <3; i++){
                        if (i != currentPlayer){
                            players[i].score += skullScore;
                            if (players[i].score < 0) players[i].score = 0;
                            playerServer[i].sendState(pState);
                            for (int j = 0; j<3; j++){
                                scores[i] = players[i].score;
                            }
                            playerServer[i].sendScore(scores);

                        }
                        System.out.println(players[i].name + " now has a score of " + players[i].score);
                    }




                }
                else {
                    players[currentPlayer].setScore(playerServer[currentPlayer].recieveScore() + players[currentPlayer].score);
                    if (players[currentPlayer].score < 0) players[currentPlayer].setScore(0);
                    System.out.println(String.format("Player %s completed their turn and their score is now %d", players[currentPlayer].name, players[currentPlayer].score));
                }

                if (pState == States.GAMEOVER){
                    for (Server p : playerServer){
                        p.terminate();
                    }
                    return;
                }

                for (Player p : players){
                    if (p.score > highscore) highscore = p.score;
                }
                if (highscore >= 3000){
                    countdown--;
                    highscore = 0;
                    System.out.println("A player has exceeded 3000 points. The game will end in " + countdown + " turns unless diminished.");
                }
                else{
                    countdown = 3;
                }

                currentPlayer++;
                switch (state){
                    case PLAYERTURN_1:
                        state = States.PLAYERTURN_2;
                        playerTurn.set(2);
                        break;
                    case PLAYERTURN_2:
                        state = States.PLAYERTURN_3;
                        playerTurn.set(3);
                        break;
                }
                if (currentPlayer == 3) {
                    currentPlayer = 0;
                    state = States.PLAYERTURN_1;
                    playerTurn.set(1);
                }

                if (countdown == 0){
                    state = States.GAMEOVER;
                    gameOver.set(true);
                    playerServer[0].sendState(state);
                    playerServer[1].sendState(state);
                    playerServer[2].sendState(state);
                }
            }
            playerServer[0].sendPlayers(players);
            playerServer[1].sendPlayers(players);
            playerServer[2].sendPlayers(players);
            System.out.println("The game is over!");
            Player winner = new Player("temp");
            winner.score = 0;
            for (Player p : players){
                if (p.score > winner.score){
                    winner = p;

                }
                System.out.println(String.format("Player %s scored %d", p.name, p.score));
            }
            System.out.println("Player " + winner.name + " has won!");
            ss.close();

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    synchronized public void gameLoop(boolean test) {
        System.out.println("Inside of gameloop");

        int highscore = 0;
        try {
            playerServer[0].sendPlayers(players);

            state = States.PLAYERTURN_1;
            while (state != States.GAMEOVER){


                playerServer[0].sendState(state);


                States pState;
                pState = playerServer[currentPlayer].receiveState();

                if (pState == States.SKULL_ISLAND){
                    System.out.println(players[currentPlayer].name + " has reached skull island!");
                    playerServer[currentPlayer].receiveState();
                    int skullScore = playerServer[currentPlayer].recieveScore();
                    System.out.println("All other players will suffer a deduction of " + skullScore);

                    int[] scores = new int[3];

                    for (int i = 0; i<3; i++){
                        scores[i] = players[i].score;
                    }

                    for (int i = 0; i <3; i++){
                        if (i != currentPlayer){
                            players[i].score += skullScore;
                            if (players[i].score < 0) players[i].score = 0;
                            playerServer[i].sendState(pState);

                            playerServer[i].sendScore(scores);

                        }
                        System.out.println(players[i].name + " now has a score of " + players[i].score);
                    }


                }
                else {
                    players[currentPlayer].setScore(playerServer[currentPlayer].recieveScore() + players[currentPlayer].score);
                    if (players[currentPlayer].score < 0) players[currentPlayer].setScore(0);
                    System.out.println(String.format("Player %s completed their turn and their score is now %d", players[currentPlayer].name, players[currentPlayer].score));
                }
                state = States.GAMEOVER;
                gameOver.set(true);

            }


        } catch (Exception e){
            e.printStackTrace();
        }


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

        public void sendPlayers(Player[] pl) {
            try {
                for (Player p : pl) {
                    dOut.writeObject(p);
                    dOut.flush();
                }

            } catch (IOException ex) {
                System.out.println("Score sheet not sent");
                ex.printStackTrace();
            }

        }



        public int recieveScore(){
            try{
                return dIn.readInt();
            }catch (IOException e){
                System.out.println(String.format("Did not recieve score from %s", players[currentPlayer].name));
                e.printStackTrace();
            }
            return -1;
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

        public void sendState(States state){
            try{
                dOut.writeObject(state);
                dOut.flush();
            } catch (Exception e){
                System.out.println("Failed to send state.");
                e.printStackTrace();
            }
        }

        public void sendScore(int[] score){
            try {
                for (int s: score){
                    dOut.writeInt(s);
                    dOut.flush();
                }

            } catch (IOException ex) {
                System.out.println("Score sheet not sent");
                ex.printStackTrace();
            }
        }

        public States receiveState(){
            try{
                return (States) dIn.readObject();
            }catch (Exception e){
                System.out.println("Could not receive state");
                e.printStackTrace();
            }
            return States.ERROR;
        }


        void terminate() throws IOException {
            socket.close();
            isRunning = false;
        }

    }

}
