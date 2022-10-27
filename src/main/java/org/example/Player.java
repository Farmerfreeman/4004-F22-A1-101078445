package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;
import java.util.zip.CheckedInputStream;


public class Player implements Serializable{


    @Serial
    private static final long serialVersionUID = 1L;
    public String name;
    public States state;

    int score = 0;

    Game game = new Game();

    int playerId = 0;
    Client clientConnection;
    Player[] players = new Player[3];

    Dice[] dice = new Dice[8];

    List<Dice> chest = new ArrayList<>();

    Cards card;

    boolean sorcUsed = false;

    public Player getPlayer() {
        return this;
    }

    public void setScore(int score){this.score = score;}

    public class Dice implements Serializable{
        Faces face;

        Boolean inChest = false;
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

    public int isDead(boolean firstRoll){
        Dictionary<Faces, Integer> dict = game.countFaces(dice);
        dict = game.handleFortuneCard(dict, card);
        if (dict.get(Faces.SKULL) >= 3){
            if (dict.get(Faces.SKULL) >= 4 && firstRoll){
                sendStateToServer(States.SKULL_ISLAND);
                return 2;
            }
            System.out.println("You have died. You will receive no points this round.");
            return 1;
        }
        else return 0;

    }

    public int scoreDice(){
        return game.scoreDice(dice, card);
    }

    public void placeInChest(String[] placed){
        if (card != Cards.TREASURE_CHEST){
            System.out.println("You don't have the treasure chest card.");
            return;
        }
        ArrayList<Integer> d = new ArrayList<Integer>();
        for (String s : placed){
            d.add(Integer.parseInt(s) - 1);
        }

        for (int s : d){
            chest.add(dice[s]);
            this.dice[s].inChest = true;
            System.out.println(chest);
        }

    }

    public void removeFromChest(String[] removed){
        ArrayList<Integer> d = new ArrayList<Integer>();
        for (String s : removed){
            d.add(Integer.parseInt(s) - 1);
        }

        for (int s : d){
            chest.remove(dice[s]);
            dice[s].inChest = false;

        }
    }



    /*
     * ----------Networking Code------------
     */


    //region Client Methods
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void sendStateToServer(States state){
        clientConnection.sendState(state);
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
                    this.score = playTurn();
                    System.out.println(String.format("You scored %d", score));
                    clientConnection.sendScore();
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

    public int playTurn(){
        Scanner scan = new Scanner(System.in);
        int score = 0;
        dice = game.rollDice(dice);
        draw();
        System.out.println(String.format("You have rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
        System.out.println(String.format("You have drawn the %s fortune card.", card.name()));
        int dead = isDead(true);
        if (dead == 2){
            System.out.println("You have reached skull island!");

        }
        else if (dead == 1){
            //This code handles if a player would have died on roll 1, but held a Sorc card.
            if (card == Cards.SORCERESS){
                while (true){
                    System.out.println("You have died, but you have a Sorcress card! Would you like to use it to reroll a skull? Y/N");
                    String choice = scan.nextLine();
                    switch (choice) {
                        case "Y":
                            for (int i = 0; i <= dice.length; i++){
                                if (dice[i].face == Faces.SKULL){
                                    dice[i] = game.useSorcress(dice[i], card);
                                    System.out.println("You rerolled a skull into a " + dice[i].face);
                                    break;
                                }
                            }
                            dead = isDead(false);
                            if(dead == 1){
                                System.out.println("Bad luck.. You got another skull and died.");
                                break;
                            }
                            else{

                                while (true) {
                                    System.out.println("Select an action:");
                                    System.out.println("(1) Score with currently held dice.");
                                    System.out.println("(2) Choose specific dice to reroll.");
                                    int act = scan.nextInt();
                                    switch (act){
                                        case 1:
                                            return scoreDice();
                                        case 2:
                                            while (true) {
                                                System.out.println("Select which die you wish to hold (Held dice are not rerolled): (1,2,4..)");
                                                String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                                                if (die.length <= 1) {
                                                    System.out.println("You must reroll at least two dice.");
                                                    continue;
                                                }
                                                else{
                                                    dice = game.reRollNotHeld(dice, die);
                                                    break;
                                                }
                                            }

                                            System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                                    }

                                    return score;
                                }
                            }
                    }
                }
            }
            return 0;
        }
        while (true) {
            //TODO: Check for player death on each roll
            System.out.println("Select an action:");
            System.out.println("(1) Score with currently held dice.");
            System.out.println("(2) Choose specific dice to reroll.");
            if (card == Cards.SORCERESS && sorcUsed == false){
                System.out.println("(3) Reroll a skull using your Sorcress card.");
            }
            if (card == Cards.TREASURE_CHEST){
                System.out.println("(3) Place di(c)e into your treasure chest for safekeeping.");
                if (!chest.isEmpty()){
                    System.out.println("(4) Remove di(c)e from your treasure chest.");
                }
            }
            int act = scan.nextInt();
            switch (act){
                case 1:
                    return scoreDice();
                case 2:
                    while (true) {
                        System.out.println("Select which die you wish to hold (Held dice are not rerolled): (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        if (die.length <= 1) {
                            System.out.println("You must reroll at least two dice.");
                            continue;
                        }
                        else{
                            dice = game.reRollNotHeld(dice, die);
                            break;
                        }
                    }

                    System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                    break;
                case 3:
                    if (card == Cards.SORCERESS) {
                        for (int i = 0; i <= dice.length; i++) {
                            if (dice[i].face == Faces.SKULL) {
                                dice[i] = game.useSorcress(dice[i], card);
                                System.out.println("You rerolled a skull into a" + dice[i].face);
                                sorcUsed = true;
                                break;
                            }
                        }
                        if (sorcUsed == false) {
                            System.out.println("You do not currently have a skull to reroll.");
                            continue;
                        }
                        break;
                    }
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which die you wish to place in the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        placeInChest(die);
                    }
                case 4:
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which die you wish to remove from the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        removeFromChest(die);
                    }
            }


        }

    }

    public int playTurn(Scanner scan){

        int score = 0;
        dice = game.rollDice(dice);
        draw();
        System.out.println(String.format("You have rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
        System.out.println(String.format("You have drawn the %s fortune card.", card.name()));
        int dead = isDead(true);
        if (dead == 2){
            System.out.println("You have reached skull island!");

        }
        else if (dead == 1){

            return 0;
        }
        while (true) {
            System.out.println("Select an action:");
            System.out.println("(1) Score with currently held dice.");
            System.out.println("(2) Choose specific dice to reroll.");
            int act = scan.nextInt();
            switch (act){
                case 1:
                    return scoreDice();
                case 2:
                    while (true) {
                        System.out.println("Select which die you wish to hold (Held dice are not rerolled): (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        if (die.length <= 1) {
                            System.out.println("You must reroll at least two dice.");
                            continue;
                        }
                        else{
                            dice = game.reRollNotHeld(dice, die);
                            break;
                        }
                    }

                    System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
            }

            return score;
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

        public void sendState(States state){
            try{
                dOut.writeObject(state);
            }catch (IOException e){
                System.out.println("State could not be sent.");
                e.printStackTrace();
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

        public void sendScore(){
            try{
                dOut.writeInt(score);
                dOut.flush();
            }catch (IOException e){
                System.out.println("Could not send score.");
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
