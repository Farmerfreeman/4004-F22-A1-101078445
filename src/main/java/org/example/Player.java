package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Player implements Serializable{


    @Serial
    private static final long serialVersionUID = 1L;
    public String name;
    public States state;

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

        public Dice(int i){
            Faces face = Faces.values()[i];
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
        clientConnection.recieveState();
        while (state != States.GAMEOVER){

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
