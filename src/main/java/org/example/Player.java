package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Player implements Serializable{


    @Serial
    private static final long serialVersionUID = 1L;
    public String name;
    public States state;

    Game game = new Game();

    int playerId = 0;
    Client clientConnection;
    Player[] players = new Player[3];

    Dice[] dice = new Dice[8];

    Cards card;

    public Player getPlayer() {
        return this;
    }

    public class Dice implements Serializable{
        Faces face;
        public Dice(){
            Faces face = Faces.DIAMOND;
        }

        public Dice(Faces i){
            face = i;
        }


    }

    public void roll(String roll){
        for (int i = 0; i < roll.length(); i++){
            int d = roll.charAt(i);
            dice[i].face = Faces.values()[(int) (Math.random() * 6)];
            System.out.println(dice[i].face);
        }

    }

    public void draw(){
        card = Cards.values()[(int) (Math.random() * 11)];
        System.out.println(card);
    }

    public boolean isPlayerTurn(){
        switch (playerId){
            case 1:
                if (state == States.PLAYERTURN_1) return true;
                else return false;
            case 2:
                if (state == States.PLAYERTURN_2) return true;
                else return false;
            case 3:
                if (state == States.PLAYERTURN_3) return true;
                else return false;
        }
        return false;
    }

    public int scoreDice(){
        return game.scoreDice(dice, card);
    }

    /*
     * ----------Networking Code------------
     */


    //region Client Methods
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void connectToClient() {
        clientConnection = new Client();
    }

    public void connectToClient(int port) {
        clientConnection = new Client(port);
    }

    public void killClient() {
        clientConnection.terminate();
    }


    public void initializePlayers() {
        for (int i = 0; i < 3; i++) {
            players[i] = new Player(" ");
        }
    }
    //endregion



    public void startGame() {
        players = clientConnection.receivePlayer();
        System.out.println(String.format("Three players have connected: %s, %s and %s", players[0].name, players[1].name, players[2].name));
        System.out.println("The game will now begin.");
        while (true){
            clientConnection.recieveState();
            if (state == States.GAMEOVER) break;
            if (isPlayerTurn()){
                System.out.println("It is your turn!");
            }
            else{
                if (state == States.PLAYERTURN_1){
                    System.out.println(String.format("%s is currently playing. Please wait...", players[0].name));
                }
                else if (state == States.PLAYERTURN_2){
                    System.out.println(String.format("%s is currently playing. Please wait...", players[1].name));
                }
                else if (state == States.PLAYERTURN_3){
                    System.out.println(String.format("%s is currently playing. Please wait...", players[2].name));
                }
            }
        }


    }



    private class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;

        public Client() {
            try {
                socket = new Socket("localhost", Config.GAME_SERVER_PORT_NUMBER);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public Client(int portId) {
            try {
                socket = new Socket("localhost", portId);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }


        public void sendPlayer() {
            try {
                dOut.writeObject(getPlayer());
                dOut.flush();
            } catch (IOException e) {
                System.out.println("Player not sent");
                e.printStackTrace();
            }
        }

        public Player[] receivePlayer() {
            Player[] pl = new Player[3];
            try {
                Player p = (Player) dIn.readObject();
                pl[0] = p;
                p = (Player) dIn.readObject();
                pl[1] = p;
                p = (Player) dIn.readObject();
                pl[2] = p;
                return pl;

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                e.printStackTrace();
            }
            return pl;
        }



        /*
         * function to send strings
         */
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("String not sent");
                ex.printStackTrace();
            }
        }

        public void recieveState(){
            try{
                state = (States) dIn.readObject();
            } catch (ClassNotFoundException e){
                System.out.println("Class not found");
                e.printStackTrace();
            } catch (IOException e){
                System.out.println("State not recieved");
                e.printStackTrace();
            }

        }


        void terminate() {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
        }

    }

    /*
     * ---------Constructor and Main class-----------
     */

    /*
     * constructor takes the name of the player and sets the score to 0
     */
    public Player(String n) {
        name = n;
        for (int i = 0; i < dice.length; i++){
            dice[i] = new Dice();
        }
    }

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();
        Player p = new Player(name);
        p.initializePlayers();
        p.connectToClient();
        p.startGame();
        myObj.close();
    }
}
